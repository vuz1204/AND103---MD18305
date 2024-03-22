const express = require("express");

const router = express.Router();

const mongoose = require("mongoose");
const RES = require("./COMMON_RES");
const uri = RES.uri;

const svModel = require("./studentModel");

router.get("/", async (req, res) => {
  await mongoose.connect(uri);

  let sinhviens = await svModel.find();

  console.log(sinhviens);

  res.send(sinhviens);
});

module.exports = router;
