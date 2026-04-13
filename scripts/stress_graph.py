import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.ticker as ticker
import matplotlib.patches as mpatches
import numpy as np

df = pd.read_csv('./results/stress/results.csv')
print("Columns:", df.columns.tolist())
print("Shape:", df.shape)
print("allThreads range:", df['allThreads'].min(), "-", df['allThreads'].max())
print("responseCode counts:\n", df['responseCode'].astype(str).value_counts())

# Normalize responseCode to string for consistent matching
df['responseCode'] = df['responseCode'].astype(str).str.strip()

# Bucket threads into groups of 10 for readability
BUCKET = 10
df['user_bucket'] = (df['allThreads'] // BUCKET) * BUCKET

# Compute median latency per (bucket, responseCode) — at least 3 samples per group
agg = (
    df.groupby(['user_bucket', 'responseCode'])
      .agg(median_rt=('elapsed', 'median'), count=('elapsed', 'count'))
      .reset_index()
)
agg = agg[agg['count'] >= 3].copy()

# Per-bucket aggregate for error-rate bar (500 + 503 together, and 503 alone)
bucket_stats = df.groupby('user_bucket').agg(
    total=('elapsed', 'count'),
    err_500=('responseCode', lambda x: (x == '500').sum()),
    err_503=('responseCode', lambda x: (x == '503').sum()),
).reset_index()
bucket_stats = bucket_stats[bucket_stats['total'] >= 5].copy()
bucket_stats['rate_500'] = bucket_stats['err_500'] / bucket_stats['total'] * 100
bucket_stats['rate_503'] = bucket_stats['err_503'] / bucket_stats['total'] * 100

# Color / style mapping
CODE_STYLE = {
    '200': {'color': '#2ca02c', 'label': 'HTTP 200 OK',                  'zorder': 3},
    '500': {'color': '#7f7f7f', 'label': 'HTTP 500 Internal Server Error','zorder': 2},
    '503': {'color': '#d62728', 'label': 'HTTP 503 Service Unavailable',  'zorder': 4},
}

fig, (ax1, ax2) = plt.subplots(2, 1, figsize=(13, 9), sharex=True,
                                gridspec_kw={'height_ratios': [3, 1]})

# --- Top plot: scatter of median latency per bucket, coloured by code ---
for code, style in CODE_STYLE.items():
    subset = agg[agg['responseCode'] == code]
    if subset.empty:
        continue
    ax1.scatter(
        subset['user_bucket'], subset['median_rt'],
        color=style['color'], label=style['label'],
        s=40, zorder=style['zorder'], alpha=0.85,
    )
    # connect dots with a thin line for readability
    ax1.plot(subset['user_bucket'], subset['median_rt'],
             color=style['color'], linewidth=0.7, alpha=0.5, zorder=style['zorder'] - 1)

ax1.axhline(y=660, color='black', linestyle='--', linewidth=1.5,
            label='SLA-порог (660 мс)', zorder=5)

# Mark where 503 first appears
first503 = agg[agg['responseCode'] == '503']['user_bucket'].min()
if not pd.isna(first503):
    ax1.axvline(x=first503, color='#d62728', linestyle=':', linewidth=1.5,
                alpha=0.8, label=f'Первые 503 ({int(first503)} польз.)', zorder=6)

ax1.set_ylabel('Медианное время ответа (мс)', fontsize=12)
ax1.set_title(
    'Стресс-тест: зависимость времени ответа от количества пользователей\n'
    '(конфигурация №3, ramp-up 0→800 пользователей за 480 с)',
    fontsize=12,
)
ax1.legend(fontsize=9, loc='upper left')
ax1.grid(True, alpha=0.3)
ax1.yaxis.set_major_formatter(ticker.FuncFormatter(lambda x, _: f'{int(x):,} мс'))
ax1.set_ylim(bottom=0)

# --- Bottom plot: stacked error rates ---
bar_width = BUCKET * 0.8
ax2.bar(bucket_stats['user_bucket'], bucket_stats['rate_500'],
        width=bar_width, color='#7f7f7f', alpha=0.7, label='HTTP 500 (%)')
ax2.bar(bucket_stats['user_bucket'], bucket_stats['rate_503'],
        width=bar_width, bottom=bucket_stats['rate_500'],
        color='#d62728', alpha=0.7, label='HTTP 503 (%)')
ax2.set_xlabel('Количество параллельных пользователей', fontsize=12)
ax2.set_ylabel('Ошибки (%)', fontsize=12)
ax2.set_ylim(0, 105)
ax2.axhline(y=5, color='orange', linestyle='--', linewidth=1, label='5% порог')
ax2.legend(fontsize=9)
ax2.grid(True, alpha=0.3, axis='y')

plt.tight_layout()
plt.savefig('./assets/image-9.png', dpi=150, bbox_inches='tight')
print("Saved to ./assets/image-9.png")

# --- Summary stats ---
print("\n=== Key breakpoints ===")
codes_200 = agg[agg['responseCode'] == '200']
over_sla = codes_200[codes_200['median_rt'] > 660]
if not over_sla.empty:
    bp = over_sla.iloc[0]
    print(f"  200 median > 660 ms starting at: {int(bp['user_bucket'])} users ({bp['median_rt']:.0f} ms)")

over_3x = codes_200[codes_200['median_rt'] > 1980]
if not over_3x.empty:
    bp = over_3x.iloc[0]
    print(f"  200 median > 3×SLA (1980 ms) starting at: {int(bp['user_bucket'])} users")

if not pd.isna(first503):
    print(f"  First HTTP 503 buckets appear at: {int(first503)} users")

dominant503 = bucket_stats[bucket_stats['rate_503'] > 50]
if not dominant503.empty:
    print(f"  503 dominates (>50% of requests) from: {int(dominant503['user_bucket'].iloc[0])} users")

print("\n=== At peak (800 users) ===")
peak_200 = codes_200[codes_200['user_bucket'] == 800]
if not peak_200.empty:
    r = peak_200.iloc[0]
    print(f"  HTTP 200 median RT = {r['median_rt']:.0f} ms")
peak_bs = bucket_stats[bucket_stats['user_bucket'] == 800]
if not peak_bs.empty:
    r = peak_bs.iloc[0]
    print(f"  Error rates — 500: {r['rate_500']:.1f}%, 503: {r['rate_503']:.1f}%")
