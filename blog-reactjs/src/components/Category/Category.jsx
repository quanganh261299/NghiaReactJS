import React, { useEffect, useState } from 'react';
import { Button, Table, Space, Breadcrumb, Modal, Input } from "antd";
import { PlusOutlined, UndoOutlined } from '@ant-design/icons';
import axios, { isCancel, AxiosError } from "axios";
import { Link } from 'react-router-dom';
import styles from './category.module.css';
import { categoryAPI } from '../../api';

const columns = [
    {
        title: 'Số thứ tự',
        dataIndex: 'id',
        key: 'id',
    },
    {
        title: 'Danh mục',
        dataIndex: 'name',
        key: 'name',
    },
];

const Category = () => {

    const [data, setData] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalElements, setTotalElements] = useState(0);

    const [isModalCreateOpen, setIsModalCreateOpen] = useState(false);
    const [isModalEditOpen, setIsModalEditOpen] = useState(false);
    const [isModalDeleteOpen, setIsModalDeleteOpen] = useState(false);

    const [currentCategory, setCurrentCategory] = useState(null);
    const [newCategoryName, setNewCategoryName] = useState('');

    const callAPI = () => {
        axios
            .get(`${categoryAPI}?page=${currentPage}`)
            .then((res) => {
                console.log("res", res);
                setData(res.data.content);
                setTotalElements(res.data.totalElements); // update total elements
                return res.data.content;
            })
            .catch((err) => console.log(err));
    }

    const showCreateModal = () => {
        setCurrentCategory(null);
        setIsModalCreateOpen(true);
    };
    const handleCreateOk = () => {
        axios.post(`${categoryAPI}`, { name: newCategoryName })
            .then((res) => {
                console.log(res);
                console.log('ok');
                callAPI();
            })
            .catch((err) => console.log(err));
        setIsModalCreateOpen(false);
    };
    
    const showEditModal = (item) => {
        setCurrentCategory(item);
        setNewCategoryName(item.name);
        setIsModalEditOpen(true);
    };
    const handleEditOk = () => {
        axios.put(`${categoryAPI}/${currentCategory.id}`, { name: newCategoryName })
            .then((res) => {
                console.log(res);
                callAPI();
            })
            .catch((err) => console.log(err));
        setIsModalEditOpen(false);
    };


    const showDeleteModal = (item) => {
        setIsModalDeleteOpen(true);
        setCurrentCategory(item);
    };

    const handleDeleteOk = () => {
        axios.delete(`${categoryAPI}/${currentCategory.id}`)
            .then(() => {
                if (data.length === 1 && currentPage > 1) {
                    setCurrentPage(currentPage - 1);
                } else {
                    callAPI();
                }
            })
            .catch((err) => console.log(err));
        setIsModalDeleteOpen(false);
    };


    useEffect(() => {
        callAPI();
    }, [currentPage])

    const finalColumns = [
        ...columns,
        {
            title: 'Chức năng',
            key: 'action',
            render: (item) => (
                <Space size="middle">
                    <Button onClick={() => showEditModal(item)}>Sửa</Button>
                    <Button onClick={() => showDeleteModal(item)}>Xóa</Button>
                </Space>
            ),
            align: 'center',
        },
    ];

    return (
        <>
            <Breadcrumb items={[
                {
                    title: <Link to={''}>Dashboard</Link>,
                },
                {
                    title: 'Category',
                },
            ]}
                className={styles.category__breadcrumb}
            />
            <Button type="primary" style={{ marginRight: 10, marginLeft: 30 }} icon={<PlusOutlined />} onClick={showCreateModal}>Thêm danh mục</Button>

            <Button type="primary" icon={<UndoOutlined />} onClick={callAPI}>Reset</Button>
            <div className={styles.category__table}>
                <Table columns={finalColumns}
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

            <Modal title="Thêm danh mục" open={isModalCreateOpen} onOk={handleCreateOk} onCancel={() => setIsModalCreateOpen(false)}>
                <Input value={newCategoryName} onChange={(e) => setNewCategoryName(e.target.value)} placeholder='Mời nhập tên danh mục mới' />
            </Modal>

            <Modal title="Sửa tên danh mục" open={isModalEditOpen} onOk={handleEditOk} onCancel={() => setIsModalEditOpen(false)}>
                <Input value={newCategoryName} onChange={(e) => setNewCategoryName(e.target.value)} placeholder='Mời nhập tên danh mục mới' />
            </Modal>

            <Modal title="Cảnh báo" open={isModalDeleteOpen} onOk={handleDeleteOk} onCancel={() => setIsModalDeleteOpen(false)}>
                Bạn có chắc muốn xóa danh mục này không?
            </Modal >
        </>
    )
}

export default Category;