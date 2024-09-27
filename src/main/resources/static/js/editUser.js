function toggleFreelancer() {
    const checkbox = document.getElementById("freelancerCheckbox");
    const companyFieldDiv = document.getElementById("companyFieldDiv");
    const companyField = document.getElementById("companyField");

    if (checkbox.checked) {
        companyFieldDiv.style.display = "none";  // Hide the company field
        companyField.value = "FREELANCER";  // Set the value to "FREELANCER"
    } else {
        companyFieldDiv.style.display = "block";  // Show the company field
        companyField.value = "";  // Clear the company field
    }
}
