/* resources/public/js/script.js - Basic JavaScript functionality */
document.addEventListener('DOMContentLoaded', function() {
    // Highlight current page in navigation
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('nav a');

    navLinks.forEach(link => {
        if (link.getAttribute('href') === currentPath) {
            link.style.color = '#3498db';
            link.style.fontWeight = '700';
        }
    });

    // Simple form validation for contact form
    const contactForm = document.querySelector('.contact-form form');

    if (contactForm) {
        contactForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const name = document.getElementById('name').value;
            const email = document.getElementById('email').value;
            const message = document.getElementById('message').value;

            if (!name || !email || !message) {
                alert('Please fill in all required fields.');
                return;
            }

            // For a real implementation, you would send this data to your server
            alert('Thank you for your message! We will get back to you soon.');
            contactForm.reset();
        });
    }
});
