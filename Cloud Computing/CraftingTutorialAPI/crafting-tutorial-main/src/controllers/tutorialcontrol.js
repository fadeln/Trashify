const express = require('express');
const materialModel = require('../db/tutorialschema');

const getTutorialByMaterial = async (req, res) => {
    try {
        const material = req.params.material;
        const tutorialByMaterial = await materialModel.findOne({ material });

        if (tutorialByMaterial && tutorialByMaterial.tutorials.length > 0) {
            // Corrected variable name from 'tutorialDocument' to 'tutorialByMaterial'
            const randomIndex = Math.floor(Math.random() * tutorialByMaterial.tutorials.length);
            const randomTutorial = tutorialByMaterial.tutorials[randomIndex];

            return res.status(200).json(randomTutorial);
        } else {
            // Return a 404 status code if no tutorial is found
            return res.status(404).json({ error: 'No tutorial found for the given material.' });
        }
    } catch (error) {
        console.error(error);
        // Return a 500 status code for server error
        return res.status(500).json({ error: 'Internal server error.' });
    }
};

module.exports = { getTutorialByMaterial };
