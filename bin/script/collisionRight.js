function update(event) {
	game_object.setPosX(event.getCollisionObject().getPosX() + event.getCollisionObject().getWidth());
	game_object.setSpeedX(0);
	game_object.sendObjects();
}

