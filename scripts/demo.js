console.log("debug from javascript!")

executor.registerFunction("test", test)

function test(properties) {
    console.log("function executed " + properties.value)
}

executor.onSignal("use_item_block", onUse)
executor.onSignal("entity_interact", onInteract)

function onUse(properties) {
    let id = Math.floor(Math.random() * 1000);
    let entity = "minecraft:skeleton"
    let blockPosition = properties.block.position;
    let position = blockPosition.x + " " + (blockPosition.y + 1) + " " + blockPosition.z
    let data = executor.run("entity editor init " + id + " " + entity + " " + position)
    if (data.success) {
        console.log("success!")
    } else {
        console.log("failure")
    }
}

function onInteract(properties) {
    let targetUuid = properties.target.uuid
    executor.run("entity kill " + targetUuid)
    console.log("entity killed")
}