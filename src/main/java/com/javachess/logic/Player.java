package com.javachess.logic;

public class Player {
  // Most likely email
  private String id;

  private Color color;

  private Player(String id, Color color) {
    this.id = id;
    this.color = color;
  }

  private Player(String id) {
    this.id = id;
  }

  public static Player of(String id) {
    return new Player(id);
  }

  public static Player of(String id, Color color) {
    return new Player(id, color);
  }

  public Color getColor() {
    return color;
  }

  public String getId() {
    return id;
  }

  public boolean equals(Object o) {
    if (o instanceof Player) {
      return ((Player) o).getId().equals(this.getId());
    }
    return false;
  }
}
