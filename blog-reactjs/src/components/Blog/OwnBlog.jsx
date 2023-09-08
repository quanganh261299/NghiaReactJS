import React, { useEffect, useState } from 'react';
import { Button, Table, Space, Breadcrumb, Tag } from "antd";
import { PlusOutlined, UndoOutlined } from '@ant-design/icons';
import axios, { isCancel, AxiosError } from "axios";
import { Link } from 'react-router-dom';
import styles from './blog.module.css';
import { ownBlogAPI } from '../../api';

const columns = [
    {
        title: 'STT',
        dataIndex: 'id',
        key: 'id',
    },
    {
        title: 'Tiêu đề',
        dataIndex: 'title',
        key: 'title',
    },
    {
        title: 'Danh mục',
        dataIndex: 'categories',
        key: 'categories',
        render: categories => (
            <>
                {categories.map(category => <Tag color="blue" key={category.id}>{category.name}</Tag>)}
            </>
        )
    },
    {
        title: 'Trạng thái',
        dataIndex: 'status',
        key: 'status',
        render: status => status == 1 ? 'Công khai' : 'Nháp'
    },
    {
        title: 'Ngày tạo',
        dataIndex: 'createdAt',
        key: 'createdAt',
    },
];

const OwnBlog = () => {
    const [data, setData] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalElements, setTotalElements] = useState(0);

    const callAPI = () => {
        axios
            .get(`${ownBlogAPI}?page=${currentPage}`)
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
                    title: 'Bài viết của tôi',
                },
            ]}
                className={styles.category__breadcrumb}
            />
            <Button type="primary" style={{ marginRight: 10, marginLeft: 30 }} icon={<PlusOutlined />} >Viết bài</Button>

            <Button type="primary" icon={<UndoOutlined />} onClick={callAPI}>Reset</Button>
            <div className={styles.category__table}>
                <Table columns={columns}
                    dataSource={data.map((item) => ({ ...item, key: item.id }))}
                    pagination={{
                        current: currentPage,
                        pageSize: 5,
                        total: totalElements,
                        position: ['bottomCenter'],
                        onChange: (page) => setCurrentPage(page),
                    }}
                />
            </div>
        </>
    )
}

export default OwnBlog;