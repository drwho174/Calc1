"""Собирает коллаж из эталонных скриншотов Roborazzi.

Запуск из корня репозитория:
    python tools/make_collage.py
Результат: docs/ui-collage.png
"""

from pathlib import Path

from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "app" / "src" / "test" / "screenshots"
OUT = ROOT / "docs" / "ui-collage.png"

ITEMS = [
    ("main_menu_light.png", "Главное меню — светлая"),
    ("main_menu_dark.png", "Главное меню — тёмная"),
    ("calc_bsa.png", "Площадь поверхности тела"),
    ("calc_ibm.png", "Идеальная масса тела"),
    ("calc_respiratory.png", "Показатели оксигенации"),
    ("calc_gfr.png", "СКФ — CKD-EPI 2021"),
    ("calc_oxygen.png", "Доставка кислорода"),
    ("calc_hb.png", "Гемоглобин на перфузии"),
    ("calc_perfusiologist.png", "Калькулятор перфузиолога"),
    ("calc_mehran.png", "Mehran Score"),
    ("calc_euroscore.png", "EuroSCORE II"),
    ("calc_euroscore_dark.png", "EuroSCORE II — тёмная"),
    ("calc_euroscore_scrolled.png", "EuroSCORE II — прокрутка"),
    ("settings_system.png", "Настройки — как на устройстве"),
    ("settings_light.png", "Настройки — светлая"),
    ("settings_dark.png", "Настройки — тёмная"),
    ("about_gfr_light.png", "Справка СКФ — markdown"),
    ("about_mehran_light.png", "Справка Mehran — таблицы"),
    ("about_app_light.png", "О программе"),
    ("about_gfr_dark.png", "Справка СКФ — тёмная"),
    ("components_light.png", "Компоненты — светлая"),
    ("components_dark.png", "Компоненты — тёмная"),
]

COLS = 4
TILE_H = 700
GAP = 22
LABEL_H = 34
TITLE_H = 64
PAD = 24
BG = (24, 24, 24)
FG = (236, 236, 236)


def load_font(size: int):
    for name in ("segoeui.ttf", "arial.ttf", "calibri.ttf"):
        candidate = Path(r"C:\Windows\Fonts") / name
        if candidate.exists():
            return ImageFont.truetype(str(candidate), size)
    return ImageFont.load_default()


def main() -> None:
    font_label = load_font(20)
    font_title = load_font(30)

    tiles = []
    for filename, label in ITEMS:
        path = SRC / filename
        if not path.exists():
            raise SystemExit(f"missing screenshot: {path}")
        img = Image.open(path).convert("RGB")
        scale = TILE_H / img.height
        resized = img.resize(
            (max(1, round(img.width * scale)), TILE_H), Image.LANCZOS
        )
        tiles.append((resized, label))

    tile_w = max(tile.width for tile, _ in tiles)
    rows = (len(tiles) + COLS - 1) // COLS
    canvas_w = PAD * 2 + COLS * tile_w + (COLS - 1) * GAP
    canvas_h = PAD * 2 + TITLE_H + rows * (TILE_H + LABEL_H) + (rows - 1) * GAP

    canvas = Image.new("RGB", (canvas_w, canvas_h), BG)
    draw = ImageDraw.Draw(canvas)
    draw.text(
        (PAD, PAD),
        "Calc1 — Compose UI (Roborazzi, Robolectric)",
        font=font_title,
        fill=FG,
    )

    for idx, (tile, label) in enumerate(tiles):
        row, col = divmod(idx, COLS)
        column_x = PAD + col * (tile_w + GAP)
        y = PAD + TITLE_H + row * (TILE_H + LABEL_H + GAP)
        canvas.paste(tile, (column_x + (tile_w - tile.width) // 2, y))
        bbox = draw.textbbox((0, 0), label, font=font_label)
        draw.text(
            (column_x + (tile_w - (bbox[2] - bbox[0])) // 2, y + TILE_H + 6),
            label,
            font=font_label,
            fill=FG,
        )

    OUT.parent.mkdir(parents=True, exist_ok=True)
    canvas.save(OUT, optimize=True)
    print(f"saved {OUT} ({canvas.width}x{canvas.height})")


if __name__ == "__main__":
    main()
