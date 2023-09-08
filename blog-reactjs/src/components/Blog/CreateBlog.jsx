import React, { useEffect, useState, useRef } from 'react';
import { Button, Table, Space, Breadcrumb, Tag, Select } from "antd";
import { SaveOutlined, SwapLeftOutlined } from '@ant-design/icons';
import axios, { isCancel, AxiosError } from "axios";
import ReactMarkdown from 'react-markdown';
import { Link, useNavigate } from 'react-router-dom';
import styles from './createblog.module.css';
import { categoryAPI, blogAPI } from '../../api';

const CreateBlog = () => {
    const navigate = useNavigate();
    const [data, setData] = useState(null);
    const [selectedStatus, setSelectedStatus] = useState(null);
    const [selectedCategories, setSelectedCategories] = useState([]);

    const inputRefTitle = useRef('');
    const inputRefContent = useRef('');
    const inputRefDescription = useRef('');

    const postData = {
        title: inputRefTitle.current.value,
        content: inputRefContent.current.value,
        description: inputRefDescription.current.value,
        status: selectedStatus,
        categories: selectedCategories,
    }

    useEffect(() => {
        axios
            .get(`${categoryAPI}?pageSize=10000`)
            .then((res) => {
                console.log("res", res);
                setData(res.data.content);
                return res.data.content;
            })
    }, []);

    const handleChange = (value) => {
        setSelectedCategories(value);
    }

    const handleChangeStatus = (value) => {
        setSelectedStatus(value);
    }

    //đã thêm cors rồi nhưng riêng đoạn này lỗi không biết có phải do em phân quyền gì không? riêng phần này lỗi?
    const handlePostData = () => {
        axios.post(`${blogAPI}/create`, postData)
            .then((res) => {
                console.log("ok");
                navigate('/admin/blogs');
            })
            .catch((err) => console.log(err));
    }

    return (
        <>
            <Breadcrumb items={[
                {
                    title: <Link to={''}>Dashboard</Link>,
                },
                {
                    title: 'Danh sách bài viết',
                },
            ]}
                className={styles.createblog__breadcrumb}
            />
            <Button type="primary" style={{ marginRight: 10, marginLeft: 35 }} icon={<SwapLeftOutlined />} onClick={() => navigate('/admin/blogs')} >Quay lại</Button>
            <Button type="primary" icon={<SaveOutlined />} onClick={handlePostData}>Lưu</Button>
            <div className={styles.createblog__container}>
                <div className={styles.createblog__leftside}>
                    <label htmlFor="title" className={styles.createblog__title}>Tiêu đề</label>
                    <input type="text" id='title' className={styles.createblog__input} ref={inputRefTitle} />
                    <label htmlFor="content" className={styles.createblog__title}>Nội dung</label>
                    <textarea type="text" id="content" style={{ height: 500, resize: 'none' }} className={`${styles.createblog__input} ${styles.createblog__textarea}`} ref={inputRefContent}></textarea>
                    <label htmlFor="description" className={styles.createblog__title}>Mô tả ngắn</label>
                    <textarea id='description' className={`${styles.createblog__input} ${styles.createblog__textarea}`} style={{ height: 100 }} ref={inputRefDescription}></textarea>
                </div>
                <div className={styles.createblog__rightside}>
                    <label htmlFor="status" className={styles.createblog__title}>Trạng thái</label>
                    <Select id="status" style={{ marginBottom: 15 }} onChange={handleChangeStatus}>
                        <Select.Option value="0">Nháp</Select.Option>
                        <Select.Option value="1">Công khai</Select.Option>
                    </Select>
                    <label htmlFor="category" className={styles.createblog__title}>Danh mục</label>
                    <Select mode="tags" style={{ width: '100%' }} placeholder="Chọn danh mục" value={selectedCategories} onChange={handleChange}>
                        {data && data.map(item => (
                            <Select.Option key={item.id} value={item.name}>{item.name}</Select.Option>
                        ))}
                    </Select>
                </div>
            </div>
        </>
    )
}

export default CreateBlog;