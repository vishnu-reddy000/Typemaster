const { execSync } = require('child_process');
const path = require('path');
const fs = require('fs');

const rootDir = __dirname;
const staticDir = path.join(rootDir, 'src', 'main', 'resources', 'static');
const cssDir = path.join(staticDir, 'assets', 'css');
const jsDir = path.join(staticDir, 'assets', 'js');

const cssFiles = [
    'auth.css',
    'blog.css',
    'home.css',
    'result.css',
    'style.css',
    'typing.css'
];

const jsFiles = [
    'app.js',
    'auth.js',
    'blog-data.js',
    'data.js',
    'stats.js',
    'timer.js',
    'typing.js'
];

console.log('--- CSS Minification ---');
cssFiles.forEach(file => {
    const input = path.join(cssDir, file);
    const output = path.join(cssDir, file.replace('.css', '.min.css'));
    console.log(`Minifying ${file} -> ${path.basename(output)}`);
    try {
        execSync(`npx clean-css-cli "${input}" -o "${output}"`, { stdio: 'inherit' });
    } catch (e) {
        console.error(`Error minifying CSS file ${file}:`, e.message);
    }
});

console.log('--- JS Minification ---');
jsFiles.forEach(file => {
    const input = path.join(jsDir, file);
    const output = path.join(jsDir, file.replace('.js', '.min.js'));
    console.log(`Minifying ${file} -> ${path.basename(output)}`);
    try {
        execSync(`npx terser "${input}" -o "${output}" --compress --mangle`, { stdio: 'inherit' });
    } catch (e) {
        console.error(`Error minifying JS file ${file}:`, e.message);
    }
});

console.log('Minification complete!');
