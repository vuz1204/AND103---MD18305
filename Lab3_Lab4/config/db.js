const mongoose = require("mongoose");
mongoose.set("strictQuery", true);

const local = "mongodb://localhost:27017/MyDatabase";

const connect = async () => {
  try {
    await mongoose.connect(local /* Truyền biến database muốn connect */, {
      useNewUrlParser: true,
      useUnifiedTopology: true,
    });
    console.log("connect success");
  } catch (error) {
    console.log(error);
    console.log("connect fail");
  }
};

module.exports = { connect };
