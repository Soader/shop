import axios from "axios";

const api = axios.create({
  url: "http://localhost:8080",
});

export const fetchOrders = async (
  filters: any,
  page: number,
  size: number,
  globalSearch: string
) => {
  const { data } = await api.get("/order-header", {
    params: { ...filters, globalSearch, page, size },
  });
  return data;
};

export default api;
