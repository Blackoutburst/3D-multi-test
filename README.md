# 3D Multi Test
This project is a Minecraft clone in OpenGL/WebGL using a custom network protocol
![image](https://github.com/Blackoutburst/3D-multi-test/assets/30992311/906bb644-0de8-405d-b252-dc319272b508)

## Clients

### Kotlin client (OpenGL)
The kotlin client is up to date and ready to use

### JavaScript client (WebGL)
The JavaScript client is not up to date

## Current Protocol

### Client bound

Identification: `0x00`
| id   | entityId |
|------|----------|
| byte | int      |

Add Entity: `0x01`
| id   | entityId | x     | y     | z     | yaw   | pitch |
|------|----------|-------|-------|-------|-------|-------|
| byte | int      | float | float | float | float | float |

Remove Entity: `0x02`
| id   | entityId |
|------|----------|
| byte | int      |


Update Entity: `0x03`
| id   | entityId | x     | y     | z     | yaw   | pitch |
|------|----------|-------|-------|-------|-------|-------|
| byte | int      | float | float | float | float | float |

Send Chunk: `0x04`
| id   | x   | y   | z   | BlockType  |
|------|-----|-----|-----|------------|
| byte | int | int | int | byte[4096] |

Send Placeholder Chunk: `0x05`
| id   | x   | y   | z   |
|------|-----|-----|-----|
| byte | int | int | int |

### Server bound
Update Entity: `0x00`
| id   | entityId | x     | y     | z     | yaw   | pitch |
|------|----------|-------|-------|-------|-------|-------|
| byte | int      | float | float | float | float | float |

Update Block: `0x01`
| id   | BlockType | x   | y   | z   |
|------|-----------|-----|-----|-----|
| byte | byte      | int | int | int |

Block Bulk Edit: `0x02`
| id   | blockCount | BlockType | x   | y   | z   | BlockType | x   | y   | z   | ... |
|------|------------|-----------|-----|-----|-----|-----------|-----|-----|-----|-----|
| byte | Int        | byte      | int | int | int | byte      | int | int | int | ... |

### BlockType
| id | Name       |
|----|------------|
| 0  | Air        |
| 1  | Grass      |
| 2  | Dirt       |
| 3  | Stone      |
| 4  | Oak Log    |
| 5  | Oak Leaves |


### Information
- Packets **Server bound** `[0x00]` check the client `entityId`. Client must save the `entityId` received by the packet **Client Bound** `0x00` upon connecting to the server. Without this ID client won't be able to move
- This protocol uses **BIG ENDIAN**
- Server doesn't send empty chunk

### Intended way to read data
First read the first byte to determine which packet you received. Then read the rest of the data
