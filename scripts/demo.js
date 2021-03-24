console.log("debug from javascript!")

// Register signals and api functions
executor.registerFunction("test", test)
executor.onSignal(signals.PLAYER_USE_ITEM_ON_BLOCK, onUse)
executor.onSignal("player_entity_interact", onInteract)

const createEntity = executor.make("entity editor create {0}")
const killEntity = executor.make("entity kill {0}")

function test(properties) {
    console.log(`function executed ${properties.value}`)

    createEntity("minecraft:skeleton 0 50 0")
}

function onUse(properties) {
    let blockPosition = properties.block.position
    blockPosition.x += 0.5
    blockPosition.y += 1
    blockPosition.z += 0.5

    let data = createEntity("minecraft:skeleton " + blockPosition)

    if (data.success) {
        console.log(`You created the entity ${data.entity.uuid} successfully!`)
    } else {
        console.log("failure")
    }
}

function onInteract(properties) {
    let targetUuid = properties.target.uuid

    killEntity(targetUuid)
    console.log("entity killed")
}

executor.run("utils map set Test:test 5")
let data = executor.run("utils map get Test:test")
console.log("number: "+(data.value+5))

executor.onSignal("player_join", (properties) => {
    console.log("join "+properties.player.username)
})