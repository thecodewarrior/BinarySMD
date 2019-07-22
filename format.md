Based on [this](https://developer.valvesoftware.com/wiki/Studiomdl_Data).

# Header

### SMD
`version 1`

### BinarySMD
```
| block count (int) |
  | block type (byte) | block |
```

# Nodes

### SMD
```
nodes
<int|ID> "<string|Bone Name>" <int|Parent ID>
end
```

### BinarySMD
```
| block type = 0 (byte) |
| count (int) |
  | id (int) | name (string) | parent (int) |
```

# Skeleton

### SMD
```
skeleton
time <int>
<int|bone ID> <float|PosX PosY PosZ> <float|RotX RotY RotZ>
end
```

### BinarySMD
```
| block type = 1 (byte) |
| vector count (int) |
  | vectorX (float) | vectorY (float) | vectorZ (float) |
| keyframe count (int) |
  | keyframe time (int) | bone count (int) |
    | bone ID (int) | pos index (int) | rot index (int) |
```

# Triangles

### SMD
```
triangles
<material>
<int|Parent bone> <float|PosX PosY PosZ> <normal|NormX NormY NormZ> <normal|U V> <int|links> <int|Bone ID>  
<normal|Weight>
end
```

### BinarySMD
```
| block type = 2 (byte) |
| material count (int) |
  | material name |
| vector count (int) |
  | vectorX (float) | vectorY (float) | vectorZ (float) |
| uv count (int) |
  | U (float) | V (float) |
| triangle count (int) |
  | material index (int) |
  | parent bone (int) | pos index (int) | normal index (int) | UV index (int) | link count (int) |
    | link bone ID (int) | link weight (float) |
  | parent bone (int) | pos index (int) | normal index (int) | UV index (int) | link count (int) |
    | link bone ID (int) | link weight (float) |
  | parent bone (int) | pos index (int) | normal index (int) | UV index (int) | link count (int) |
    | link bone ID (int) | link weight (float) |
```

# Vertexanimation

### SMD
```
vertexanimation
time <int>
<int|ID> <float|PosX PosY PosZ> <float|NormX NormY NormZ>
end
```

### BinarySMD
```
| block type = 3 (byte) |
| vector count (int) |
  | vectorX (float) | vectorY (float) | vectorZ (float) |
| frame count (int) |
  | frame time (int) | vertex count (int) |
    | vertex ID (int) | pos index (int) | normal index (int) |
```

