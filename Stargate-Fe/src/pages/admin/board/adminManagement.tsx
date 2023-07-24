import React from 'react';
import AdminBoardHeaderNav from '../../../components/atoms/AdminBoardHeaderNav'
import AdminManagementModal from '../../../components/organisms/AdminManagementModal';

const adminManagement = () => {
  return <div className='w-screen h-screen'>
    <AdminBoardHeaderNav />
    <div className='flex flex-col justify-center items-center'>
      <p className='form-title bg-black'>소속 연예인 관리</p>
      <AdminManagementModal />
    </div>
  </div>;
};

export default adminManagement;
