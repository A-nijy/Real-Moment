import axios from "axios";

const axiosInstanceWithoutAuth = axios.create({
  baseURL: "/api",
});

export default axiosInstanceWithoutAuth;
