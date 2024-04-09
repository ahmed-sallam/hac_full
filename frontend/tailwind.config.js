/** @type {import('tailwindcss').Config} */
module.exports = {
    content: ["./src/**/*.{html,ts}",
        "./node_modules/flowbite/**/*.js"],
    theme: {
        colors: {
            primary: "#E3A011",
            secondary: "#0A0804",
            accent: "#5C3F07",
            mustard: "#7C5C0C",
            white: "#fff",
            danger: "#FF0000",
            success: "#18ba18"
        },
        fontFamily: {
            sans: ["Cairo", "Tajawal", "Roboto", "sans-serif"],
            Tajawal: ["Tajawal", "sans-serif"],
            Cairo: ["Cairo", "sans-serif"],
        },
        extend: {},
    },
    plugins: [
        require('flowbite/plugin')
    ],
}

