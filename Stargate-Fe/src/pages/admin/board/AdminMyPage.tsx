import { useEffect, useState } from 'react';
import BoardHeaderNav from '@/atoms/board/BoardHeaderNav';
import MyPageBox from '@/organisms/board/MyPageBox';
import { fetchAdminData } from '@/services/adminBoardService';
import { AdminData } from '@/types/board/type';

const AdminMyPage = () => {
  const [adminData, setAdminData] = useState<AdminData | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      const data = await fetchAdminData();
      setAdminData(data);
    };
    fetchData();
  }, []);

  return (
    <div>
      <BoardHeaderNav isAdmin={true}/>
      <div className="flex w-full justify-center items-center">
        <MyPageBox
          isAdmin={true}
          email={adminData?.email || ''}
          name={adminData?.name || ''}
          code={adminData?.code || ''}
        />
      </div>
    </div>
  );
};

export default AdminMyPage;
