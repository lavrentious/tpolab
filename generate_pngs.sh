#!/bin/bash

SCRIPT_DIR=$(dirname "$0")
for csv_file in "$SCRIPT_DIR/report/plots/csv"/*.csv; do
    base_name=$(basename "$csv_file" .csv)
    python "$SCRIPT_DIR/scripts/plot_function.py" "$csv_file" --output "$SCRIPT_DIR/report/plots/${base_name}.png"
    echo "generated for $base_name"
done