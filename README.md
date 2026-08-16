# pillow

[Pillow](https://python-pillow.github.io/) for z/OS — the imaging library.

Unblocks `fpdf2`, `python-pptx` and `rst2pdf`, each of which was waiting on it
alone, and is one of the pieces `seaborn` needs.

## Install

```sh
zopen install pillow
```

or, into a virtual environment:

```sh
export PIP_EXTRA_INDEX_URL="https://repo.zopen.community/pypi/wheels/simple/"
export PIP_CONSTRAINT="https://repo.zopen.community/pulp/content/constraints/zopen-constraints.txt"
pip install pillow
```

## What works

JPEG and PNG are both compiled in, against
[zlib](https://github.com/zopencommunity/zlibport) and
[libjpeg-turbo](https://github.com/zopencommunity/libjpeg-turboport). Verified
on 3.12, 3.13 and 3.14:

- PNG round trips **exactly** — it is lossless, so any difference would be a bug
- JPEG at q95 round trips within **2 of 255**, greyscale within 1
- progressive JPEG, resize, rotate, crop, mode conversion, drawing and filters
- BMP, GIF and PPM, which Pillow implements itself

Formats needing libraries not yet ported — TIFF, WebP, JPEG 2000, and FreeType
text rendering — are not available. Pillow works without them.

## Notes for this platform

Two things the build needs that are not obvious.

`-D_OPEN_THREADS=3` is required before anything compiles at all: Python's own
`cpython/pythread.h` uses `pthread_t`, which is undeclared without it, and the
first extension fails with `unknown type name 'pthread_t'` pointing inside the
interpreter's headers — which reads as a broken Python rather than a missing
feature-test macro.

The extensions link against zoslib, or the binder ends with RC 12 on
`__open_ascii` and `__close` — which reads as a corrupt library rather than a
short link line.

Both go on `CFLAGS`/`LDFLAGS` directly. `ZOPEN_EXTRA_CFLAGS` is composed into
the flags for the configure and make path and never reaches a setuptools build.
