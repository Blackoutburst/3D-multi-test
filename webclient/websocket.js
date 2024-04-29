export function connectWebSocket() {
    const ws = new WebSocket("ws://192.168.0.20:16000/game")

    ws.onopen = () => {
        ws.send("hello")
    }

    ws.onmessage = (e) => {
        e.data.arrayBuffer().then((buffer) => {
            const dataView = new DataView(buffer)
            const id = dataView.getInt8(0)
            const x = dataView.getInt32(1, false)
            const y = dataView.getInt32(5, false)
            const z = dataView.getInt32(9, false)
            const data = []
            
            for (let i = 13; i < 4109; i++)
                data.push(dataView.getInt8(i))
            
            console.log("id", id)
            console.log("x", x)
            console.log("y", y)
            console.log("z", z)
            console.log("data", data)
        })
    }

    ws.onerror = () => {
    }

    ws.onclose = () => {
    }
}