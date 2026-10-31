extends Node2D
## Forest Village demo scene.
## Builds the sample village from demo_map.json using the pack's TileSet and prop sprites.
## Needs Godot 4.3+ (TileMapLayer). Arrow keys / WASD pan, mouse wheel zooms.

const TILESET: TileSet = preload("res://forest_village/forest_village_tileset.tres")
const MAP_PATH := "res://forest_village/demo/demo_map.json"
const PROP_DIR := "res://forest_village/props/"
const TILE := 16

var _camera: Camera2D


func _ready() -> void:
	texture_filter = CanvasItem.TEXTURE_FILTER_NEAREST
	var data: Dictionary = JSON.parse_string(FileAccess.get_file_as_string(MAP_PATH))

	var ground := _make_layer("Ground", -10)
	for t in data.ground:
		ground.set_cell(Vector2i(t[0], t[1]), t[2], Vector2i(t[3], t[4]))

	var structures := _make_layer("Structures", -9)
	for t in data.structures:
		structures.set_cell(Vector2i(t[0], t[1]), t[2], Vector2i(t[3], t[4]))

	# Props are plain Sprite2D nodes so they can sit at exact pixel positions.
	# Each sprite's origin is moved to its "ground" line, which makes y-sorting work.
	var objects := Node2D.new()
	objects.name = "Objects"
	objects.y_sort_enabled = true
	add_child(objects)
	for i in data.sprites.size():
		var s: Dictionary = data.sprites[i]
		var sprite := Sprite2D.new()
		sprite.texture = load("%s%s/%s.png" % [PROP_DIR, s.category, s.name])
		sprite.centered = false
		sprite.offset = Vector2(0, -s.ground)
		sprite.position = Vector2(s.x, s.y + s.ground)
		if i < data.decor_count:
			sprite.z_index = -8  # flat ground decoration (flowers, lily pads) sits under everything
		objects.add_child(sprite)

	_camera = Camera2D.new()
	_camera.position = Vector2(data.width, data.height) * TILE * 0.5
	_camera.zoom = Vector2(1.65, 1.65)
	add_child(_camera)


func _make_layer(layer_name: String, z: int) -> TileMapLayer:
	var layer := TileMapLayer.new()
	layer.name = layer_name
	layer.tile_set = TILESET
	layer.z_index = z
	add_child(layer)
	return layer


func _process(delta: float) -> void:
	var dir := Input.get_vector("ui_left", "ui_right", "ui_up", "ui_down")
	_camera.position += dir * 400.0 * delta / _camera.zoom.x


func _unhandled_input(event: InputEvent) -> void:
	if event is InputEventMouseButton and event.pressed:
		if event.button_index == MOUSE_BUTTON_WHEEL_UP:
			_camera.zoom = (_camera.zoom * 1.25).clamp(Vector2(1, 1), Vector2(6, 6))
		elif event.button_index == MOUSE_BUTTON_WHEEL_DOWN:
			_camera.zoom = (_camera.zoom / 1.25).clamp(Vector2(1, 1), Vector2(6, 6))
