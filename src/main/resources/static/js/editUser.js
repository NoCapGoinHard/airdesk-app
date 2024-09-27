document.addEventListener("DOMContentLoaded", function () {
    const companyInput = document.getElementById("companyField");

    // Function to fetch and display company suggestions
    companyInput.addEventListener("input", function () {
        const query = companyInput.value.trim();

        if (query.length >= 2) { // Start suggesting after 2 characters
            fetch(`/company-suggestions?query=${encodeURIComponent(query)}`)
                .then(response => response.json())
                .then(data => {
                    displayCompanySuggestions(data);
                })
                .catch(error => {
                    console.error("Error fetching company suggestions:", error);
                });
        }
    });

    // Display the dropdown with company suggestions
    function displayCompanySuggestions(suggestions) {
        const suggestionsDropdown = document.getElementById("companySuggestions");

        // Clear previous suggestions
        suggestionsDropdown.innerHTML = "";

        // Add each suggestion as a dropdown option
        suggestions.forEach(suggestion => {
            const option = document.createElement("div");
            option.textContent = suggestion;
            option.classList.add("suggestion-item");

            option.addEventListener("click", function () {
                companyInput.value = suggestion;
                suggestionsDropdown.innerHTML = ""; // Clear suggestions after selection
            });

            suggestionsDropdown.appendChild(option);
        });
    }
});

// Toggle the company input field based on the freelancer checkbox
function toggleFreelancer() {
    const companyFieldDiv = document.getElementById("companyFieldDiv");
    const freelancerCheckbox = document.getElementById("freelancerCheckbox");
    const companyInput = document.getElementById("companyField");

    if (freelancerCheckbox.checked) {
        companyInput.value = "FREELANCER";
        companyFieldDiv.style.display = "none";
    } else {
        companyFieldDiv.style.display = "block";
        companyInput.value = "";
    }
}
