const express = require("express");

const tutorialRouter = require("./tutorialrouter");

const router = express.Router();


module.exports = () => {
    tutorialRouter(router);
    return router;
};