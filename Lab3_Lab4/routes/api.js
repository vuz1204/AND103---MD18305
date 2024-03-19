var express = require("express");
var router = express.Router();

// Thêm model
const Distributors = require("../models/distributors");
const Fruits = require("../models/fruits");

// Api thêm distributor
router.post("/add-distributor", async (req, res) => {
  try {
    const data = req.body; // Lấy dữ liệu từ body
    const newDistributors = new Distributors({
      name: data.name,
    }); // Tạo một đối tượng mới
    const result = await newDistributors.save(); // Thêm vào database
    if (result) {
      // Nếu thêm thành công result !null trả về dữ liệu
      res.json({
        status: 200,
        messenger: "Thêm thành công",
        data: result,
      });
    } else {
      // Nếu thêm không thành công result null, thông báo không thành công
      res.json({
        status: 400,
        messenger: "Lỗi, thêm không thành công",
        data: [],
      });
    }
  } catch (error) {
    console.log(error);
  }
});

// Api thêm fruit
router.post("/add-fruit", async (req, res) => {
  try {
    const data = req.body; // Lấy dữ liệu từ body
    const newFruit = new Fruits({
      name: data.name,
      quantity: data.quantity,
      price: data.price,
      status: data.status,
      image: data.image,
      description: data.description,
      id_distributor: data.id_distributor,
    }); // Tạo một đối tượng mới
    const result = await newFruit.save(); // Thêm vào database
    if (result) {
      // Nếu thêm thành công result !null trả về dữ liệu
      res.json({
        status: 200,
        messenger: "Thêm thành công",
        data: result,
      });
    } else {
      // Nếu thêm không thành công result null, thông báo không thành công
      res.json({
        status: 400,
        messenger: "Lỗi, thêm không thành công",
        data: [],
      });
    }
  } catch (error) {
    console.log(error);
  }
});

// Api get danh sách fruit
router.get("/get-list-fruit", async (req, res, next) => {
  const authHeader = req.headers["authorization"];
  // Authorization thêm từ khóa 'Bearer token'
  // nên sẽ xử lý cắt chuỗi
  const token = authHeader && authHeader.split(" ")[1];
  // Nếu không có token sẽ trả về 401
  if (token == null) return res.sendStatus(401);
  let payload;
  JWT.verify(token, SECRETKEY, (err, _payload) => {
    // Kiểm tra token, nếu token không đúng, hoặc hết hạn
    // Trả status code 403
    // Trả status hết hạn 401 khi token hết hạn
    if (err instanceof JWT.TokenExpiredError) return res.sendStatus(401);
    if (err) return res.sendStatus(403);
    // Nếu đúng sẽ log ra dữ liệu
    payload = _payload;
  });
  console.log(payload);
  try {
    const data = await Fruits.find().populate("id_distributor");
    res.json({
      status: 200,
      messenger: "Danh sách fruit",
      data: data,
    });
  } catch (error) {
    console.log(error);
  }
});

// Get chi tiết Fruits
router.get("/get-fruit-by-id/:id", async (req, res) => {
  // :id param
  try {
    const { id } = req.params; // Lấy dữ liệu thông qua :id trên url gọi là param
    const data = await Fruits.findById(id).populate("id_distributor");
    res.json({
      status: 200,
      messenger: "Danh sách fruit",
      data: data,
    });
  } catch (error) {
    console.log(error);
  }
});

router.get("/get-list-fruit-in-price", async (req, res) => {
  try {
    const { price_start, price_end } = req.query;

    const query = { price: { $gte: price_start, $lte: price_end } };
    // $gte lớn hơn hoặc bằng, $ge lớn hơn
    // $lte nhỏ hơn hoặc bằng, $le nhỏ hơn
    // truyền câu điều kiện, và chỉ lấy các trường hợp mong muốn
    const data = await Fruits.find(query, "name quantity price id_distributor")
      .populate("id_distributor")
      .sort({ quantity: -1 }) // giảm dần = -1, tăng dần = 1
      .skip(0) // bỏ qua số lượng row
      .limit(2); // lấy 2 sản phẩm
    res.json({
      status: 200,
      messenger: "Danh sách fruit",
      data: data,
    });
  } catch (error) {
    console.log(error);
  }
});

router.get("/get-list-fruit-have-name-a-or-x", async (req, res) => {
  try {
    const query = {
      $or: [{ name: { $regex: "A" } }, { name: { $regex: "X" } }],
    };

    // truyền câu điều kiện, và chỉ lấy các trường hợp mong muốn
    const data = await Fruits.find(
      query,
      "name quantity price id_distributor"
    ).populate("id_distributor");

    res.json({
      status: 200,
      messenger: "Danh sách fruit",
      data: data,
    });
  } catch (error) {
    console.log(error);
  }
});

// Api cập nhật fruit
router.put("/update-fruit-by-id/:id", async (req, res) => {
  // :id param
  try {
    const { id } = req.params; // Lấy dữ liệu thông qua :id trên url gọi là param
    const data = req.body; // Lấy dữ liệu từ body
    const updateFruit = await Fruits.findById(id);
    let result = null;
    if (updateFruit) {
      updateFruit.name = data.name ?? updateFruit.name;
      updateFruit.quantity = data.quantity ?? updateFruit.quantity;
      updateFruit.price = data.price ?? updateFruit.price;
      updateFruit.status = data.status ?? updateFruit.status;
      updateFruit.image = data.image ?? updateFruit.image;
      updateFruit.description = data.description ?? updateFruit.description;
      updateFruit.id_distributor =
        data.id_distributor ?? updateFruit.id_distributor;
      result = await updateFruit.save();
    }
    // Tạo một đối tượng mới
    // Thêm vào database
    if (result) {
      // Nếu thêm thành công result !null trả về dữ liệu
      res.json({
        status: 200,
        messenger: "Cập nhật thành công",
        data: result,
      });
    } else {
      res.json({
        status: 400,
        messenger: "Lỗi, cập nhật không thành công",
        data: [],
      });
    }
  } catch (error) {
    console.log(error);
  }
});

// Api xóa một fruit
router.delete("/destroy-fruit-by-id/:id", async (req, res) => {
  // :id param
  try {
    const { id } = req.params; // Lấy dữ liệu thông qua :id trên url gọi là param
    const result = await Fruits.findByIdAndDelete(id);
    if (result) {
      // Nếu xóa thành công sẽ trả về thông tin item đã xóa
      res.json({
        status: 200,
        messenger: "Xóa thành công",
        data: result,
      });
    } else {
      res.json({
        status: 400,
        messenger: "Lỗi, xóa không thành công",
        data: [],
      });
    }
  } catch (error) {
    console.log(error);
  }
});

const Upload = require("../config/common/upload");
router.post(
  "/add-fruit-with-file-image",
  Upload.array("image", 5),
  async (req, res) => {
    // Upload.array('image', 5) => up nhiều file tối đa là 5
    // Upload.single('image') => up load 1 file
    try {
      const data = req.body; // Lấy dữ liệu từ body
      const { files } = req; // files nếu upload nhiều, file nếu upload 1 file
      const urlsImage = files.map(
        (file) =>
          `${req.protocol}://${req.get("host")}/uploads/${file.filename}`
      );
      // url hình ảnh sẽ được lưu dưới dạng: http://localhost:3000/upload/filename
      const newFruit = new Fruits({
        name: data.name,
        quantity: data.quantity,
        price: data.price,
        status: data.status,
        image: urlsImage /* Thêm url hình */,
        description: data.description,
        id_distributor: data.id_distributor,
      }); // Tạo một đối tượng mới
      const result = await newFruit.save(); // Thêm vào database
      if (result) {
        // Nếu thêm thành công result !null trả về dữ liệu
        res.json({
          status: 200,
          messenger: "Thêm thành công",
          data: result,
        });
      } else {
        res.json({
          status: 400,
          messenger: "Lỗi, thêm không thành công",
          data: [],
        });
      }
    } catch (error) {
      console.log(error);
    }
  }
);

const Users = require("../models/users");
const Transporter = require("../config/common/mail");
router.post(
  "/register-send-email",
  Upload.single("avatar"),
  async (req, res) => {
    try {
      const data = req.body;
      const { file } = req;
      const newUser = Users({
        username: data.username,
        password: data.password,
        email: data.email,
        name: data.name,
        avatar: `${req.protocol}://${req.get("host")}/uploads/${file.filename}`,
        // url avatar http://localhost:3000/uploads/filename
      });
      const result = await newUser.save(); // Thêm vào database
      if (result) {
        // Gửi mail
        const mailOptions = {
          from: "vanvu101204@gmail.com", // Email gửi đi
          to: result.email, // Email nhận
          subject: "Đăng ký thành công", // Subject
          text: "Cảm ơn bạn đã đăng ký", // Nội dụng mail
        };
        // Nếu thêm thành công result !null trả về dữ liệu
        await Transporter.sendMail(mailOptions); // Gửi mail
        res.json({
          status: 200,
          messenger: "Thêm thành công",
          data: result,
        });
      } else {
        // Nếu thêm không thành công result null, thông báo không thành công
        res.json({
          status: 400,
          messenger: "Lỗi, thêm không thành công",
          data: [],
        });
      }
    } catch (error) {
      console.log(error);
    }
  }
);

const JWT = require("jsonwebtoken");
const SECRETKEY = "FPTPOLYTECHNIC";
router.post("/login", async (req, res) => {
  try {
    const { username, password } = req.body;
    const user = await Users.findOne({ username, password });
    if (user) {
      // Token người dùng sẽ sử dụng gửi lên trên header mỗi lần muốn gọi api
      const token = JWT.sign({ id: user._id }, SECRETKEY, { expiresIn: "1h" });
      // Khi token hết hạn, người dùng sẽ call 1 api khác để lấy token mới
      // Lúc này người dùng sẽ truyền refreshToken lên để nhận về 1 cặp token, refreshToken mới
      // Nếu cả 2 token đều hết hạn người dùng sẽ phải thoát app và đăng nhập lại
      const refreshToken = JWT.sign({ id: user._id }, SECRETKEY, {
        expiresIn: "1d",
      });
      // expiresIn thời gian token
      // Nếu thêm thành công result !null trả về dữ liệu
      res.json({
        status: 200,
        messenger: "Đăng nhập thành công",
        data: user,
        token: token,
        refreshToken: refreshToken,
      });
    } else {
      res.json({
        status: 400,
        messenger: "Lỗi, đăng nhập không thành công",
        data: [],
      });
    }
  } catch (error) {}
});

module.exports = router;
