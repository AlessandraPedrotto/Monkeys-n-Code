function changeLanguage(lang) {
  // Store the selected language in localStorage
  localStorage.setItem('selectedLanguage', lang);

  // Load the corresponding JSON file for the selected language
  fetch(`/traduzioni/${lang}.json`)
    .then(response => response.json())
    .then(translations => {
      // Find all elements with the data-translate attribute
      const elementsToTranslate = document.querySelectorAll("[data-translate]");

      // Replace the text of each element with the appropriate translation
      elementsToTranslate.forEach(element => {
        const key = element.getAttribute("data-translate");
        if (translations[key]) {
                    // Update text content for non-input elements
                    if (element.tagName !== "INPUT" && element.tagName !== "TEXTAREA") {
                        element.textContent = translations[key];
                    } else {
                        // Update the placeholder for input and textarea elements
                        element.placeholder = translations[key];
                    }
                }
      });swalTranslations = {
                successTitle: translations["validate_success_title"],
                errorTitle: translations["validate_error_title"],
                successMessage: translations["validate_success_message"],
                errorMessage: translations["validate_error_message"]
            };
         toastTranslations = {
                addSuccess: translations["toast_add_success"],
                removeSuccess: translations["toast_remove_success"],
                cardNotPresent: translations["toast_card_not_present"],
                addError: translations["toast_add_error"],
                removeError: translations["toast_remove_error"]
            }; 
      const urlParams = new URLSearchParams(window.location.search);
            const fromCollection = urlParams.get('from');

            if (fromCollection === 'myCollection') {
                document.title = translations['title_collection']; // Change the page title
                const pageTitleElement = document.querySelector("h1");
                if (pageTitleElement) {
                    pageTitleElement.textContent = translations['title_collection']; // Change the H1 text
                }
            }
      const allGreetings = document.querySelectorAll('#userGreeting span');
            allGreetings.forEach(span => {
                if (span.getAttribute('data-lang') === lang) {
                    span.style.display = 'inline';  // Show the selected language's greeting
                } else {
                    span.style.display = 'none';    // Hide other greetings
                }
            });
	  updateLabels(lang);
      // Change the slogan image based on the selected language
      const sloganImage = document.getElementById("immagine-slogan");
      if (lang === 'it') {
        sloganImage.src = '/image/slogan-italiano.png';
      } else if (lang === 'en') {
        sloganImage.src = '/image/slogan-inglese.png';
      } else if (lang === 'es') {
        sloganImage.src = '/image/slogan-spagnolo.png';
      }

      const seguiciImage = document.getElementById("immagine-seguici");
      if (lang === 'it') {
        seguiciImage.src = '/image/seguici-italiano.png';
      } else if (lang === 'en') {
        seguiciImage.src = '/image/seguici-inglese.png';
      } else if (lang === 'es') {
        seguiciImage.src = '/image/seguici-spagnolo.png';
      }
    })
    .catch(error => console.error('Errore nel caricamento delle traduzioni:', error));
}

// Set the default language (Italian) when the page loads
document.addEventListener("DOMContentLoaded", function() {
  const savedLanguage = localStorage.getItem('selectedLanguage') || 'it'; // Italiano di default
  changeLanguage(savedLanguage);
});