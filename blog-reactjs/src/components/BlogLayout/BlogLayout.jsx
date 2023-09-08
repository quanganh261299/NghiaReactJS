import React, { useState } from 'react';
import {
    CaretRightOutlined,
    PieChartOutlined,
    BookOutlined,
    UserOutlined,
    MenuFoldOutlined,
    MenuUnfoldOutlined,
    FullscreenOutlined,
    FullscreenExitOutlined
} from '@ant-design/icons';
import styles from './bloglayout.module.css';
import logo from '../../pictures/admin_logo.png'
import userAvatar from '../../pictures/user-avatar.jpg'
import { Popover, Layout, Menu, Button } from 'antd';
import { Outlet, Link } from 'react-router-dom';


const { Header, Content, Footer, Sider } = Layout;


function getItem(label, key, icon, children) {
    return {
        key,
        icon,
        children,
        label,
    };
}
const items = [
    getItem('Quản lí bài viết', 'article', <PieChartOutlined />, [
        getItem(<Link to='/admin/blogs'>Tất cả bài viết</Link>, 'allArticle', <CaretRightOutlined />),
        getItem(<Link to='/admin/blogs/own-blog'>Bài viết của tôi</Link>, 'myArticle', <CaretRightOutlined />),
        getItem(<Link to='/admin/blogs/create'>Tạo bài viết</Link>, 'createArticle', <CaretRightOutlined />),
    ]),
    getItem('Quản lí user', 'user', <UserOutlined />, [
        getItem(<Link to='/admin/user'>Danh sách user</Link>, 'allUser', <CaretRightOutlined />),
        getItem('Tạo user', 'createUser', <CaretRightOutlined />),
    ]),
    getItem(<Link to='/admin/category'>Category</Link>, 'category', <BookOutlined />),
];

const BlogLayout = () => {
    const [collapsed, setCollapsed] = useState(false);
    const [btnFullScreen, setBtnFullScreen] = useState(true);
    const [current, setCurrent] = useState(() => {
        const storedValue = localStorage.getItem('currentNavigation');
        return storedValue;
    });

    const fullScreen = () => {
        if (!document.fullscreenElement) {
            if (document.documentElement.requestFullscreen) {
                document.documentElement.requestFullscreen();
                setBtnFullScreen(false);
            }
        } else {
            if (document.exitFullscreen) {
                document.exitFullscreen();
                setBtnFullScreen(true);
            }
        }
    }

    const onClick = (e) => {
        setCurrent(e.key);
        localStorage.setItem('currentNavigation', e.key);
    };

    const content = () => {
        return (
            <>
                <div onClick={(e) => {
                    e.preventDefault();
                }} className={styles.bloglayout__dropdown_menu}>Trang chủ</div>
                <div onClick={(e) => {
                    e.preventDefault();
                }} className={styles.bloglayout__dropdown_menu}>Đăng xuất</div>
            </>
        )
    }


    return (
        <Layout className={styles.bloglayout__layout}>
            <Sider trigger={null} collapsible collapsed={collapsed} onCollapse={(value) => setCollapsed(value)}>
                <div className="demo-logo-vertical">
                    <img src={logo} alt="logo admin" width={collapsed ? 80 : 200} />
                </div>
                <Menu theme="dark" onClick={onClick} selectedKeys={[current]} mode="inline" items={items} />
            </Sider>
            <Layout>
                <Header className={styles.bloglayout__header}>
                    <Button
                        type="text"
                        icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
                        onClick={() => setCollapsed(!collapsed)}
                        className={styles.bloglayout__btn_menu}
                    />
                    <div className={styles.bloglayout__right_menu}>
                        <Popover content={content} trigger="click">
                            <img src={userAvatar} alt="user avatar" className={styles.bloglayout__user_avatar} />
                        </Popover>
                        {btnFullScreen ? <FullscreenOutlined onClick={fullScreen} style={{ fontSize: 20 }} /> : <FullscreenExitOutlined onClick={fullScreen} style={{ fontSize: 20 }} />}
                    </div>
                </Header>
                <Content>
                    <Outlet />
                </Content>
                <Footer className={styles.bloglayout__footer}>
                    Blog © 2023 Created by Nghia
                </Footer>
            </Layout>
        </Layout>
    );
}

export default BlogLayout;