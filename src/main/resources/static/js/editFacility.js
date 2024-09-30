// Adding a new company field
function addCompanyField() {
    document.getElementById('newCompanyField').style.display = 'block';
    console.log('Company field added');
}

// Track the number of buildings, floors, and rooms dynamically added
let buildingCount = 0;
let floorCounts = {};
let roomCounts = {};
let workstationCounts = {};

// Adding a new building dynamically
function addBuilding() {
    const buildingsDiv = document.getElementById('buildings');
    const buildingIndex = buildingCount++;
    floorCounts[buildingIndex] = 0;

    const buildingDiv = document.createElement('div');
    buildingDiv.classList.add('building');
    buildingDiv.innerHTML = `
        <h4>Building #${buildingIndex + 1}</h4>
        <div>
            <label for="buildings[${buildingIndex}].name">Building Name</label>
            <input type="text" name="buildings[${buildingIndex}].name" required>
        </div>
        <div>
            <label for="buildings[${buildingIndex}].address.street">Street</label>
            <input type="text" name="buildings[${buildingIndex}].address.street" required>
            <label for="buildings[${buildingIndex}].address.city">City</label>
            <input type="text" name="buildings[${buildingIndex}].address.city" required>
            <label for="buildings[${buildingIndex}].address.state">State</label>
            <input type="text" name="buildings[${buildingIndex}].address.state" required>
            <label for="buildings[${buildingIndex}].address.postalCode">Postal Code</label>
            <input type="text" name="buildings[${buildingIndex}].address.postalCode" required>
            <label for="buildings[${buildingIndex}].address.number">Number</label>
            <input type="text" name="buildings[${buildingIndex}].address.number" required>
        </div>
        <h5>Opening Hours</h5>
        <div id="building-${buildingIndex}-opening-hours"></div>
        <button type="button" onclick="addBuildingOpeningHours(${buildingIndex})">Add Opening Hours</button>
        <div id="floors-${buildingIndex}">
            <!-- Floors will be added here -->
        </div>
        <button type="button" onclick="addFloor(${buildingIndex})">Add Floor</button>
    `;
    buildingsDiv.appendChild(buildingDiv);

    console.log(`Building ${buildingIndex + 1} added`);
}

// Adding a new floor dynamically
function addFloor(buildingIndex) {
    const floorsDiv = document.getElementById(`floors-${buildingIndex}`);
    const floorIndex = floorCounts[buildingIndex]++;
    roomCounts[`${buildingIndex}-${floorIndex}`] = 0;

    const autoAssignedFloorNumber = floorIndex + 1;  // Automatically assign the floor number in order

    const floorDiv = document.createElement('div');
    floorDiv.classList.add('floor');
    floorDiv.innerHTML = `
        <h5>Floor #${autoAssignedFloorNumber}</h5>
        <div>
            <label for="buildings[${buildingIndex}].floors[${floorIndex}].number">Floor Number</label>
            <input type="number" name="buildings[${buildingIndex}].floors[${floorIndex}].number" value="${autoAssignedFloorNumber}" readonly>
        </div>
        <div id="rooms-${buildingIndex}-${floorIndex}">
            <!-- Rooms will be added here -->
        </div>
        <button type="button" onclick="addRoom(${buildingIndex}, ${floorIndex})">Add Room</button>
    `;
    floorsDiv.appendChild(floorDiv);
    console.log(`Floor ${autoAssignedFloorNumber} added to Building ${buildingIndex + 1}`);
}

// Adding a new room dynamically
function addRoom(buildingIndex, floorIndex) {
    const roomsDiv = document.getElementById(`rooms-${buildingIndex}-${floorIndex}`);
    const roomIndex = roomCounts[`${buildingIndex}-${floorIndex}`]++;
    workstationCounts[`${buildingIndex}-${floorIndex}-${roomIndex}`] = 0;

    const roomDiv = document.createElement('div');
    roomDiv.classList.add('room');
    roomDiv.innerHTML = `
        <h6>Room #${roomIndex + 1}</h6>
        <div>
            <label for="buildings[${buildingIndex}].floors[${floorIndex}].rooms[${roomIndex}].name">Room Name</label>
            <input type="text" name="buildings[${buildingIndex}].floors[${floorIndex}].rooms[${roomIndex}].name" required>
        </div>
        <h6>Opening Hours</h6>
        <div id="room-${buildingIndex}-${floorIndex}-${roomIndex}-opening-hours"></div>
        <button type="button" onclick="addRoomOpeningHours(${buildingIndex}, ${floorIndex}, ${roomIndex})">Add Opening Hours</button>
        <div id="workstations-${buildingIndex}-${floorIndex}-${roomIndex}">
            <!-- Workstations will be added here -->
        </div>
        <button type="button" onclick="addWorkstation(${buildingIndex}, ${floorIndex}, ${roomIndex})">Add Workstation</button>
    `;
    roomsDiv.appendChild(roomDiv);
    console.log(`Room ${roomIndex + 1} added to Floor ${floorIndex + 1} of Building ${buildingIndex + 1}`);
}

// Adding a new workstation dynamically
function addWorkstation(buildingIndex, floorIndex, roomIndex) {
    const workstationsDiv = document.getElementById(`workstations-${buildingIndex}-${floorIndex}-${roomIndex}`);
    const workstationIndex = workstationCounts[`${buildingIndex}-${floorIndex}-${roomIndex}`]++;

    const workstationDiv = document.createElement('div');
    workstationDiv.classList.add('workstation');
    workstationDiv.innerHTML = `
        <h6>Workstation #${workstationIndex + 1}</h6>
        <div>
            <label for="buildings[${buildingIndex}].floors[${floorIndex}].rooms[${roomIndex}].workstations[${workstationIndex}].workstationId">Workstation ID</label>
            <input type="text" name="buildings[${buildingIndex}].floors[${floorIndex}].rooms[${roomIndex}].workstations[${workstationIndex}].workstationId" required>
            <label for="buildings[${buildingIndex}].floors[${floorIndex}].rooms[${roomIndex}].workstations[${workstationIndex}].workstationType">Workstation Type</label>
            <select name="buildings[${buildingIndex}].floors[${floorIndex}].rooms[${roomIndex}].workstations[${workstationIndex}].workstationType">
                <option value="PC_WORKSTATION">PC Workstation</option>
                <option value="SEATING_AREA">Seating Area</option>
                <option value="MEETING_ROOM">Meeting Room</option>
            </select>
        </div>
    `;
    workstationsDiv.appendChild(workstationDiv);
    console.log(`Workstation ${workstationIndex + 1} added to Room ${roomIndex + 1}`);
}

// Adding opening hours for a building
function addBuildingOpeningHours(buildingIndex) {
    console.log(`Adding Building Opening Hours for buildingIndex: ${buildingIndex}`);

    const openingHoursDiv = document.getElementById(`building-${buildingIndex}-opening-hours`);
    if (!openingHoursDiv) {
        console.error("Opening hours div not found for building!");
        return;
    }

    const index = openingHoursDiv.children.length;

    const openingHoursRow = document.createElement('div');
    openingHoursRow.classList.add('opening-hours');
    openingHoursRow.innerHTML = `
        <div>
            <label for="buildings[${buildingIndex}].openingHours[${index}].day">Day of Week</label>
            <select name="buildings[${buildingIndex}].openingHours[${index}].day">
                <option value="MONDAY">Monday</option>
                <option value="TUESDAY">Tuesday</option>
                <option value="WEDNESDAY">Wednesday</option>
                <option value="THURSDAY">Thursday</option>
                <option value="FRIDAY">Friday</option>
                <option value="SATURDAY">Saturday</option>
                <option value="SUNDAY">Sunday</option>
            </select>
            <label for="buildings[${buildingIndex}].openingHours[${index}].startingTime">Start Time</label>
            <input type="time" name="buildings[${buildingIndex}].openingHours[${index}].startingTime">
            <label for="buildings[${buildingIndex}].openingHours[${index}].endingTime">End Time</label>
            <input type="time" name="buildings[${buildingIndex}].openingHours[${index}].endingTime">
        </div>
    `;
    openingHoursDiv.appendChild(openingHoursRow);
}

// Adding opening hours for a room
function addRoomOpeningHours(buildingIndex, floorIndex, roomIndex) {
    console.log(`Adding Room Opening Hours for buildingIndex: ${buildingIndex}, floorIndex: ${floorIndex}, roomIndex: ${roomIndex}`);

    const openingHoursDiv = document.getElementById(`room-${buildingIndex}-${floorIndex}-${roomIndex}-opening-hours`);
    if (!openingHoursDiv) {
        console.error("Opening hours div not found for room!");
        return;
    }

    const index = openingHoursDiv.children.length;

    const openingHoursRow = document.createElement('div');
    openingHoursRow.classList.add('opening-hours');
    openingHoursRow.innerHTML = `
        <div>
            <label for="buildings[${buildingIndex}].floors[${floorIndex}].rooms[${roomIndex}].openingHours[${index}].day">Day of Week</label>
            <select name="buildings[${buildingIndex}].floors[${floorIndex}].rooms[${roomIndex}].openingHours[${index}].day">
                <option value="MONDAY">Monday</option>
                <option value="TUESDAY">Tuesday</option>
                <option value="WEDNESDAY">Wednesday</option>
                <option value="THURSDAY">Thursday</option>
                <option value="FRIDAY">Friday</option>
                <option value="SATURDAY">Saturday</option>
                <option value="SUNDAY">Sunday</option>
            </select>
            <label for="buildings[${buildingIndex}].floors[${floorIndex}].rooms[${roomIndex}].openingHours[${index}].startingTime">Start Time</label>
            <input type="time" name="buildings[${buildingIndex}].floors[${floorIndex}].rooms[${roomIndex}].openingHours[${index}].startingTime">
            <label for="buildings[${buildingIndex}].floors[${floorIndex}].rooms[${roomIndex}].openingHours[${index}].endingTime">End Time</label>
            <input type="time" name="buildings[${buildingIndex}].floors[${floorIndex}].rooms[${roomIndex}].openingHours[${index}].endingTime">
        </div>
    `;
    openingHoursDiv.appendChild(openingHoursRow);
}
