// Adding a new company field
function addCompanyField() {
    document.getElementById('newCompanyField').style.display = 'block';
}

// Track the current index for buildings, floors, and rooms
let buildingIndex = 0;
let floorIndexMap = {}; // To track the number of floors for each building
let roomIndexMap = {};  // To track the number of rooms for each floor in a building

// Adding a new building dynamically
function addBuilding() {
    const buildingsDiv = document.getElementById('buildings');
    const newBuilding = document.createElement('div');
    newBuilding.className = 'building';
    floorIndexMap[buildingIndex] = 0;  // Initialize floor index for this building

    newBuilding.innerHTML = `
        <h4>Building #${buildingIndex + 1}</h4>
        <div>
            <label>Name</label>
            <input type="text" name="buildings[${buildingIndex}].name" placeholder="Building Name" required>
        </div>
        <div>
            <label>Address</label>
            <input type="text" name="buildings[${buildingIndex}].address.street" placeholder="Street" required>
            <input type="text" name="buildings[${buildingIndex}].address.city" placeholder="City" required>
            <input type="text" name="buildings[${buildingIndex}].address.state" placeholder="State" required>
            <input type="text" name="buildings[${buildingIndex}].address.postalCode" placeholder="Postal Code" required>
            <input type="text" name="buildings[${buildingIndex}].address.number" placeholder="Street Number" required>
        </div>
        <div id="floors${buildingIndex}">
            <!-- Floors will be added here -->
        </div>
        <button type="button" onclick="addFloor(${buildingIndex})">Add Floor</button>
    `;

    buildingsDiv.appendChild(newBuilding);
    buildingIndex++;
}

// Adding a new floor dynamically
function addFloor(buildingIndex) {
    const floorsDiv = document.getElementById(`floors${buildingIndex}`);
    const newFloor = document.createElement('div');
    newFloor.className = 'floor';
    const floorIndex = floorIndexMap[buildingIndex]++;
    roomIndexMap[`${buildingIndex}_${floorIndex}`] = 0;  // Initialize room index for this floor

    newFloor.innerHTML = `
        <h5>Floor #${floorIndex + 1}</h5>
        <div>
            <label>Floor Number</label>
            <input type="number" name="buildings[${buildingIndex}].floors[${floorIndex}].number" value="${floorIndex + 1}" readonly>
        </div>
        <div id="rooms${buildingIndex}_${floorIndex}">
            <!-- Rooms will be added here -->
        </div>
        <button type="button" onclick="addRoom(${buildingIndex}, ${floorIndex})">Add Room</button>
    `;

    floorsDiv.appendChild(newFloor);
}

// Adding a new room dynamically
function addRoom(buildingIndex, floorIndex) {
    const roomsDiv = document.getElementById(`rooms${buildingIndex}_${floorIndex}`);
    const roomIndex = roomIndexMap[`${buildingIndex}_${floorIndex}`]++;

    const newRoom = document.createElement('div');
    newRoom.className = 'room';

    newRoom.innerHTML = `
        <h6>Room #${roomIndex + 1}</h6>
        <div>
            <label>Room Name</label>
            <input type="text" name="buildings[${buildingIndex}].floors[${floorIndex}].rooms[${roomIndex}].name" placeholder="Room Name" required>
        </div>
        <div>
            <label>Opening Hours</label>
            <input type="time" name="buildings[${buildingIndex}].floors[${floorIndex}].rooms[${roomIndex}].openingHours[0].startingTime" required>
            <input type="time" name="buildings[${buildingIndex}].floors[${floorIndex}].rooms[${roomIndex}].openingHours[0].endingTime" required>
        </div>
    `;

    roomsDiv.appendChild(newRoom);
}
