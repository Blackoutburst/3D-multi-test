# Current Protocol

## Client bound
Move Entity: `0x00`
| id   | entityId | x     | y     | z     |
|------|----------|-------|-------|-------|
| byte | int      | float | float | float |


Add Entity: `0x01`
| id   | entityId | x     | y     | z     | yaw   | pitch |
|------|----------|-------|-------|-------|-------|-------|
| byte | int      | float | float | float | float | float |

Remove Entity: `0x02`
| id   | entityId |
|------|----------|
| byte | int      |

Identification: `0x03`
| id   | entityId |
|------|----------|
| byte | int      |

Entity Rotation: `0x04`
| id   | entityId | yaw   | pitch |
|------|----------|-------|-------|
| byte | int      | float | float |

Send Chunk: `0x05`
| id   | x   | y   | z   | BlockType  |
|------|-----|-----|-----|------------|
| byte | int | int | int | byte[4096] |

## Server bound
Move Entity: `0x00`
| id   | entityId | x     | y     | z     |
|------|----------|-------|-------|-------|
| byte | int      | float | float | float |

Entity Rotation: `0x01`
| id   | entityId | yaw   | pitch |
|------|----------|-------|-------|
| byte | int      | float | float |

Update Block: `0x02`
| id   | BlockType | x   | y   | z   |
|------|-----------|-----|-----|-----|
| byte | byte      | int | int | int |

## BlockType
```
0: Air
1: Grass
```

## Limitation
Packet size is `5000 bytes` (r/w)

## Information
Packets **Server bound** `[0x00, 0x01]` check the client `entityId`. Client must save the `entityId` received by the packet **Client Bound** `0x03` upon connecting to the server. Without this ID client wont be able to move.\
This protocol uses **BIG ENDIAN**
