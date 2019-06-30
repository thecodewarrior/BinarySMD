Based on [this](https://developer.valvesoftware.com/wiki/Studiomdl_Data).

# Header

### SMD
`version 1`

### BinarySMD
N/A

# Nodes

### SMD
```
nodes
<int|ID> "<string|Bone Name>" <int|Parent ID>
end
```

### BinarySMD
```
| count (short) |
  | id (short) | name (string) | parent (short) |
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
| frame count (short) |
  | frame time (int) | bone count (short) |
    | bone ID (short) | posX (float) | posY (float) | posZ (float) | rotX (float) | rotY (float) | rotZ (float) |
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
| material count (byte) |
  | material name |
| vector count (int) |
  | vectorX (float) | vectorY (float) | vectorZ (float) |
| triangle count (int) |
  | material index (byte) |
  | parent bone (short) | pos index (int) | normal index (int) | U (float) | V (float) | link count (byte) |
    | link bone ID (short) | link weight (float) |
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
| vector count (int) |
  | vectorX (float) | vectorY (float) | vectorZ (float) |
| frame count (short) |
  | frame time (int) | vertex count (int) |
    | vertex ID (int) | pos index (int) | normal index (int) |
```

