import React from 'react';
import AdminBoardHeaderNav from '../atoms/AdminBoardHeaderNav'
import BoardHeaderTitle from '@/atoms/BoardHeaderTitle';

const AdminBoardHeader = () => {
  return (
    <div className='w-full'>
      <AdminBoardHeaderNav/>
      <BoardHeaderTitle/>
    </div>
  );
};

export default AdminBoardHeader;