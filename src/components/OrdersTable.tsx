import React, { useState, useEffect } from "react";
import { useQuery } from "@tanstack/react-query";
import { fetchOrders } from "../apis/api";
import { useTable, useSortBy, usePagination, useFilters } from "react-table";

const OrdersTable: React.FC = () => {
  const [filters, setFilters] = useState({});
  const [maxTimeForFilters, setMaxTimeForFilters] = useState({});
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(3);
  const [searchInput, setSearchInput] = useState("");
  const [searchQuery, setSearchQuery] = useState("");

  useEffect(() => {
    const handler = setTimeout(() => {
      setMaxTimeForFilters(filters);
    }, 1500);
    return () => clearTimeout(handler);
  }, [filters]);

  const { data, isLoading, error } = useQuery({
    queryKey: ["orders", maxTimeForFilters, page, size, searchQuery],
    queryFn: () =>
      fetchOrders({ ...maxTimeForFilters }, page, size, searchQuery),
  });

  const columns = React.useMemo(
    () => [
      { Header: "Order ID", accessor: "orderId" },
      { Header: "Order Date", accessor: "orderDate" },
      { Header: "Status", accessor: "status" },
      { Header: "Customer Email", accessor: "customerEmail" },
      { Header: "Order Line ID", accessor: "orderLineId" },
      { Header: "Quantity", accessor: "quantity" },
      { Header: "Product Name", accessor: "productName" },
    ],
    []
  );

  const columnsWithFilters = [
    "orderId",
    "status",
    "customerEmail",
    "productName",
  ];

  const processedData = React.useMemo(() => {
    return (data?.content || []).flatMap((order) =>
      order.orderLines.map((line) => ({
        orderId: order.orderId,
        orderDate: order.orderDate,
        status: order.status,
        customerEmail: order.customerEmail,
        orderLineId: line.orderLineId,
        quantity: line.quantity,
        productName: line.productName,
      }))
    );
  }, [data]);

  const handleFilterChange = (columnId: string, value: string) => {
    setFilters((p) => ({
      ...p,
      [columnId]: value,
    }));
  };

  const { getTableProps, getTableBodyProps, headerGroups, rows, prepareRow } =
    useTable(
      {
        columns,
        data: processedData,
      },
      useFilters,
      useSortBy,
      usePagination
    );

  return (
    <div>
      <div className="search-bar">
        <input
          type="text"
          placeholder="Global Search..."
          value={searchInput}
          onChange={(e) => setSearchInput(e.target.value)}
        />
        <button onClick={() => setSearchQuery(searchInput)}>Search</button>
      </div>
      <table {...getTableProps()}>
        <thead>
          {headerGroups.map((headerGroup) => (
            <tr {...headerGroup.getHeaderGroupProps()}>
              {headerGroup.headers.map((column) => (
                <th {...column.getHeaderProps(column.getSortByToggleProps())}>
                  {column.render("Header")}
                  <span>
                    {column.isSorted ? (column.isSortedDesc ? "▼" : "▲") : ""}
                  </span>
                  {columnsWithFilters.includes(column.id) && (
                    <div>
                      <input
                        type="text"
                        placeholder={`Filter ${column.id}`}
                        onChange={(e) =>
                          handleFilterChange(column.id, e.target.value)
                        }
                      />
                    </div>
                  )}
                </th>
              ))}
            </tr>
          ))}
        </thead>
        <tbody {...getTableBodyProps()}>
          {rows.map((row) => {
            prepareRow(row);
            return (
              <tr {...row.getRowProps()}>
                {row.cells.map((cell) => (
                  <td {...cell.getCellProps()}>{cell.render("Cell")}</td>
                ))}
              </tr>
            );
          })}
        </tbody>
      </table>
      <div className="pagination">
        <button
          onClick={() => setPage((prev) => Math.max(prev - 1, 0))}
          disabled={page === 0}
        >
          Previous
        </button>
        <span>
          Page {page + 1} of {data?.totalPages || 1}
        </span>
        <button
          onClick={() =>
            setPage((prev) =>
              data && prev + 1 < data.totalPages ? prev + 1 : prev
            )
          }
          disabled={!data || page + 1 >= data.totalPages}
        >
          Next
        </button>
      </div>
    </div>
  );
};

export default OrdersTable;
