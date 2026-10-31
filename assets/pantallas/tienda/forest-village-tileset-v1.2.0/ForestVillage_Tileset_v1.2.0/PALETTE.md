# Forest Village palette (FV32)

32 colours, arranged as short hue-shifted ramps: shadows lean blue/purple, highlights lean yellow/green. Every opaque pixel in every PNG of this pack uses exactly one of these colours.

Light comes from the top-left. Each material has its own dark outline colour instead of one black line (foliage `fol0`, wood and dirt `earth0`, stone `stone0`).

Props carry one extra non-palette element: a soft ground shadow, drawn with `ink` (#1b1726) at roughly 33% opacity (about 20% on bridges). Delete those pixels if you want to cast your own shadows.

Palette files: `palette/forest_village_32.gpl` (GIMP, Krita, Aseprite, Pixelorama), `palette/forest_village_32.hex` (Lospec style, one hex per line), `palette/forest_village_32_strip.png` (32x1 image for palette import) and `palette/forest_village_32_swatches.png` (labelled overview).

## Outline

| Name | Hex | Used for |
| --- | --- | --- |
| `ink` | `#1b1726` | Deepest outline/shadow; lamp posts, door gaps, sunflower seeds |

## Foliage and grass

| Name | Hex | Used for |
| --- | --- | --- |
| `fol0` | `#173a2f` | Foliage outline |
| `fol1` | `#20552f` | Foliage shadow, bank edge |
| `fol2` | `#2f7435` | Foliage/grass dark, tufts |
| `fol3` | `#479040` | Grass base, foliage mid |
| `fol4` | `#66ab48` | Foliage light, grass highlights |
| `fol5` | `#8cc656` | Foliage highlight |
| `fol6` | `#b6dc6e` | Brightest leaf sparkle (birch, autumn) |

## Earth, wood and sand

| Name | Hex | Used for |
| --- | --- | --- |
| `earth0` | `#3e2a2a` | Wood/dirt outline |
| `earth1` | `#5e3d33` | Wood shadow, wet earth |
| `earth2` | `#86593d` | Wood/dirt base |
| `earth3` | `#b07f4c` | Wood light, path shade |
| `earth4` | `#d3a869` | Sand path base, straw, wood highlight |

## Stone

| Name | Hex | Used for |
| --- | --- | --- |
| `stone0` | `#2f3142` | Rock outline |
| `stone1` | `#4c5266` | Cobble mortar, rock shadow |
| `stone2` | `#747b8c` | Stone shade |
| `stone3` | `#a1a8b2` | Stone base |
| `stone4` | `#d0d4d6` | Stone light |

## Water and blue cloth/roofs

| Name | Hex | Used for |
| --- | --- | --- |
| `water0` | `#1d4a7a` | Deep water, blue roof shadow |
| `water1` | `#2c6ea8` | Water base, blue roof |
| `water2` | `#4a95c9` | Shallows, glass |
| `water3` | `#7fc1e0` | Shore light, glass light |
| `water4` | `#c4ebf0` | Foam, sparkle |

## Terracotta red

| Name | Hex | Used for |
| --- | --- | --- |
| `red0` | `#7c2a33` | Roof shadow, apple shade |
| `red1` | `#aa3f3a` | Roof base, apples |
| `red2` | `#d4663f` | Roof light, pumpkins, poppies |

## Plaster cream

| Name | Hex | Used for |
| --- | --- | --- |
| `cream0` | `#b39a74` | Plaster shade |
| `cream1` | `#dcc89a` | Plaster base, flour |
| `cream2` | `#f6f1dc` | Plaster light, white flowers |

## Flower accents

| Name | Hex | Used for |
| --- | --- | --- |
| `yellow` | `#f2cf4a` | Lantern glow, buttercups, sunflowers |
| `pink` | `#e5668f` | Blossoms, tulips |
| `purple` | `#8d62c4` | Bluebells, potion |

## Recolouring tips

- Swap whole ramps, not single colours. Replace `red0..red2` with `water0..water2` and a red roof becomes a blue roof (the pack already does this for the blue house).
- The autumn oak is the green oak with `fol0..fol6` mapped to `earth0, red0, red1, red2, earth4, yellow, cream2`.
- Keep the darkest colour of a ramp for outlines and the lightest for small highlights only.
