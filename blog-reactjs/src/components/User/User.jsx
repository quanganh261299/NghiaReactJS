import React, { useEffect, useState } from 'react';
import { Button, Table, Space, Breadcrumb, Modal, Input } from "antd";
import { Link } from 'react-router-dom';
import axios, { isCancel, AxiosError } from "axios";
import { userAPI } from '../../api';
import styles from './user.module.css';

const columns = [
    {
        title: 'Số thứ tự',
        dataIndex: 'id',
        key: 'id',
    },
    {
        title: 'Tên',
        dataIndex: 'name',
        key: 'name',
    },
    {
        title: 'Email',
        dataIndex: 'email',
        key: 'email',
    },
    {
        title: 'Role',
        dataIndex: 'role',
        key: 'role'
    }
];

const User = () => {

    const [data, setData] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalElements, setTotalElements] = useState(0);

    const callAPI = () => {
        axios
            .get(`${userAPI}?page=${currentPage}`)
            .then((res) => {
                console.log("res", res);
                setData(res.data.content);
                setTotalElements(res.data.totalElements); // update total elements
                return res.data.content;
            })
            .catch((err) => console.log(err));
    }

    useEffect(() => {
        callAPI();
    }, [currentPage])

    return (
        <>
            <Breadcrumb items={[
                {
                    title: <Link to={''}>Dashboard</Link>,
                },
                {
                    title: 'User',
                },
            ]}
                className={styles.user__breadcrumb}
            />
            <div className={styles.user__table}>
                <Table columns={columns}
                    dataSource={data.map((item) => ({ ...item, key: item.id }))}
                    pagination={{
                        current: currentPage,
                        pageSize: 10,
                        total: totalElements,
                        position: ['bottomCenter'],
                        onChange: (page) => setCurrentPage(page),
                    }}
                />
            </div>
        </>
    )
}

export default User;