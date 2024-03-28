const mongoose = require("mongoose");

const StudentSchema = mongoose.Schema({
  tenSV: {
    type: String,
    required: true,
  },
  maSV: {
    type: String,
    required: true,
  },
  diemTB: {
    type: Number,
  },
  avatar: {
    type: String,
  }
});

const StudentModel = new mongoose.model("student", StudentSchema);

module.exports = StudentModel;
