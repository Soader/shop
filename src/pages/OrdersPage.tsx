import React from "react";
import OrdersTable from "../components/OrdersTable";

const OrdersPage: React.FC = () => {
  return (
    <div>
      <h1>Orders</h1>
      <OrdersTable />
    </div>
  );
};

export default OrdersPage;
