import { world } from "./main.js"
import { Vector3 } from "./vector3.js"

export function connectWebSocket() {
    const ws = new WebSocket("ws://localhost:16000/game")

    ws.onopen = () => {}

    ws.onmessage = (e) => {
        e.data.arrayBuffer().then((buffer) => {
            parsePacket(new DataView(buffer))
        })
    }

    ws.onerror = () => {
    }

    ws.onclose = () => {
    }
}

function parsePacket(dataView) {
    const id = dataView.getInt8(0)

    switch (id) {
        case 0x00: moveEntity(dataView); break;
        case 0x01: addEntity(dataView); break;
        case 0x02: removeEntity(dataView); break;
        case 0x03: identification(dataView); break;
        case 0x04: entityRotation(dataView); break;
        case 0x05: sendChunk(dataView); break;
    }

}

function moveEntity(dataView) {
    const entityId = dataView.getInt32(1, false)
    const x =  dataView.getFloat32(5, false)
    const y =  dataView.getFloat32(9, false)
    const z =  dataView.getFloat32(13, false)

    console.log("Move", entityId, x, y, z)
}
function addEntity(dataView) {
    const entityId = dataView.getInt32(1, false)
    const x =  dataView.getFloat32(5, false)
    const y =  dataView.getFloat32(9, false)
    const z =  dataView.getFloat32(13, false)
    const yaw =  dataView.getFloat32(17, false)
    const pitch =  dataView.getFloat32(21, false)

    console.log("Add", entityId, x, y, z, yaw, pitch)
}
function removeEntity(dataView) {
    const entityId = dataView.getInt32(1, false)

    console.log("Remove", entityId)
}
function identification(dataView) {
    const entityId = dataView.getInt32(1, false)

    console.log("Identification", entityId)
}
function entityRotation(dataView) {
    const entityId = dataView.getInt32(1, false)
    const yaw =  dataView.getFloat32(5, false)
    const pitch =  dataView.getFloat32(9, false)

    console.log("Rotation", entityId, yaw, pitch)
}
function sendChunk(dataView) {
    const x = dataView.getInt32(1, false)
    const y = dataView.getInt32(5, false)
    const z = dataView.getInt32(9, false)
    const data = []

    for (let i = 13; i < 4109; i++)
        data.push(dataView.getInt8(i))

    world.updateChunk(new Vector3(x, y, z), data)
}