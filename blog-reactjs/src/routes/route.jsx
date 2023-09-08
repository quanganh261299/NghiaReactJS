import Blog from '../components/Blog/Blog';
import CreateBlog from '../components/Blog/CreateBlog';
import OwnBlog from '../components/Blog/OwnBlog';
import BlogLayout from '../components/BlogLayout/BlogLayout';
import Category from '../components/Category/Category';
import Error from '../components/Error/Error';
import User from '../components/User/User';

export const route = [
  {
    path: "/",
    element: <BlogLayout />,
    errorElement: <Error />,
    children: [
      {
        path: "/admin/blogs",
        element: <Blog />
      },
      {
        path: "/admin/blogs/own-blog",
        element: <OwnBlog />
      },
      {
        path: "/admin/blogs/create",
        element: <CreateBlog />
      },
      {
        path: "/admin/category",
        element: <Category />
      },
      {
        path: "/admin/user",
        element: <User />
      }
    ]
  },
]