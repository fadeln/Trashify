const express = require("express");

const { getTutorialByMaterial } = require("../controllers/tutorialcontrol");

module.exports = function (router = express.Router) {
    router.get('/tutorial/:material', getTutorialByMaterial);
};