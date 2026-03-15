from __future__ import annotations

import csv
import math
from decimal import Decimal, ROUND_HALF_EVEN
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
RESOURCES = ROOT / "app" / "src" / "test" / "resources"
SCALE = Decimal("0.0000001")


def q(value: float) -> str:
    return str(Decimal(str(value)).quantize(SCALE, rounding=ROUND_HALF_EVEN))


def write_rows(path: Path, rows: list[tuple[str, str]]) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    with path.open("w", newline="") as handle:
        writer = csv.writer(handle)
        writer.writerow(["x", "y"])
        writer.writerows(rows)


module_inputs = {
    "cos.csv": [-3.0, -2.5, -2.0, -1.0, -0.5, 0.5, 1.0, 2.0, 2.5, 3.0],
    "tan.csv": [-3.0, -2.0, -1.0, -0.5, 0.5, 1.0, 2.0, 3.0],
    "cot.csv": [-3.0, -2.0, -1.0, -0.5, 0.5, 1.0, 2.0, 3.0],
    "sec.csv": [-3.0, -2.0, -1.0, 0.0, 1.0, 2.0, 3.0],
    "csc.csv": [-2.5, -2.0, -1.0, -0.5, 0.5, 1.0, 2.0, 2.5],
    "log3.csv": [0.2, 0.5, 0.8, 1.2, 2.0, 5.0, 10.0, 25.0],
    "log5.csv": [0.2, 0.5, 0.8, 2.0, 5.0, 10.0, 25.0],
    "log10.csv": [0.2, 0.5, 0.8, 1.2, 2.0, 5.0, 10.0, 25.0],
}

module_functions = {
    "cos.csv": math.cos,
    "tan.csv": math.tan,
    "cot.csv": lambda x: math.cos(x) / math.sin(x),
    "sec.csv": lambda x: 1 / math.cos(x),
    "csc.csv": lambda x: 1 / math.sin(x),
    "log3.csv": lambda x: math.log(x, 3),
    "log5.csv": lambda x: math.log(x, 5),
    "log10.csv": lambda x: math.log(x, 10),
}

integration_inputs = {
    "integration/tanIT.csv": [-3.0, -2.0, -1.0, -0.5, 0.5, 1.0, 2.0, 3.0],
    "integration/cotIT.csv": [-3.0, -2.0, -1.0, -0.5, 0.5, 1.0, 2.0, 3.0],
    "integration/secIT.csv": [-3.0, -2.0, -1.0, 0.0, 1.0, 2.0, 3.0],
    "integration/cscIT.csv": [-2.5, -2.0, -1.0, -0.5, 0.5, 1.0, 2.0, 2.5],
}

integration_functions = {
    "integration/tanIT.csv": math.tan,
    "integration/cotIT.csv": lambda x: math.cos(x) / math.sin(x),
    "integration/secIT.csv": lambda x: 1 / math.cos(x),
    "integration/cscIT.csv": lambda x: 1 / math.sin(x),
}


for name, values in module_inputs.items():
    rows = [(q(x), q(module_functions[name](x))) for x in values]
    write_rows(RESOURCES / name, rows)

for name, values in integration_inputs.items():
    rows = [(q(x), q(integration_functions[name](x))) for x in values]
    write_rows(RESOURCES / name, rows)
