

const themes = [ 'latte', 'frappe', 'macchiato', 'mocha' ] as const;
type Theme = typeof themes[number];

type ThemeData = Record<Theme, {
    name: string;
    colors: {
        [key: string]: {
            hex: string;
        }
    }
}>


async function main(theme: Theme) {
    const themesData: ThemeData = await (await fetch("https://raw.githubusercontent.com/catppuccin/palette/refs/heads/main/palette.json")).json();
    
    console.log(`/* Catppuccin theme: ${themesData[theme].name} */`);
    Object.entries(themesData[theme].colors).forEach(([key, { hex }]) => {
        console.log(`-fx-color-${key}: ${hex};`);
    })
}

const theme = process.argv[2] as Theme;
if (!themes.includes(theme)) {
    console.error(`Invalid theme. Available themes are: ${themes.join(", ")}`);
} else {
    main(theme);
}

