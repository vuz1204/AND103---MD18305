const express = require("express");
const mongoose = require("mongoose");
const bodyParser = require("body-parser");
const app = express();
const port = 3000;

const RES = require("./COMMON_RES");
const uri = RES.uri;
const svModel = require("./studentModel");

app.use(express.json());
app.use(express.urlencoded({ extended: true }));

mongoose.connect(uri, { useNewUrlParser: true, useUnifiedTopology: true });

app.get("/", async (req, res) => {
  let sinhviens = await svModel.find();
  res.send(sinhviens);
});

app.post("/add-sinhVien", async (req, res) => {
  try {
    const { tenSV, maSV, diemTB, avatar } = req.body;

    const newStudent = new svModel({
      tenSV,
      maSV,
      diemTB,
      avatar
    });

    await newStudent.save();

    res.status(201).json({
      status: 201,
      message: "Student added successfully",
      data: newStudent
    });
  } catch (error) {
    // Handle errors
    console.log(error);
    res.status(500).json({
      status: 500,
      message: "Internal Server Error",
      error: error.message
    });
  }
});

app.put("/update-sinhVien-by-id/:id", async (req, res) => {
  try {
    const { id } = req.params;
    const data = req.body;
    const result = await svModel.findByIdAndUpdate(id, data, { new: true });
    if (result) {
      res.json({
        status: 200,
        message: "update successfully",
        data: result,
      });
    } else {
      res.json({
        status: 400,
        message: "update failed",
        data: null,
      });
    }
  } catch (error) {
    console.log(error);
    res.status(500).send("Internal Server Error");
  }
});

app.delete("/delete-sinhVien-by-id/:id", async (req, res) => {
  try {
    const { id } = req.params;
    const result = await svModel.findByIdAndDelete(id);
    if (result) {
      res.json({
        status: 200,
        message: "Deleted successfully", 
        data: result,
      });
    } else {
      res.status(404).json({ 
        status: 404,
        message: "Student not found",
        data: null,
      });
    }
  } catch (error) {
    console.log(error);
    res.status(500).json({ 
      status: 500,
      message: "Internal Server Error",
      error: error.message
    });
  }
});


app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(500).send("Something broke!");
});

app.listen(port, () => {
  console.log(`Example app listening on port ${port}`);
});
