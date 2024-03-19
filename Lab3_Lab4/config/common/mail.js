var nodemailer = require("nodemailer");
const transporter = nodemailer.createTransport({
  service: "gmail",
  auth: {
    user: "vanvu101204@gmail.com", // Email gửi đi
    pass: "fzqq xxts fkxi ywrn", // Mật khẩu email gửi đi
  },
});

module.exports = transporter;
