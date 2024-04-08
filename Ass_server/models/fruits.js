const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const Fruits = new Schema(
  {
    name: { type: String },
    quantity: { type: Number },
    price: { type: Number },
    status: { type: Number }, // status = 1 => còn hàng, 0 => hết hàng, -1 => ngừng kinh doanh
    image: { type: Array },
    description: { type: String },
    id_distributor: { type: Schema.Types.ObjectId, ref: "distributor" },
  },
  {
    timestamps: true,
  }
);

module.exports = mongoose.model("fruit", Fruits);
/*
    type: Schema.Types.ObjectId => kiểu dữ liệu id của mongodb
    ref: khóa ngoại
*/
