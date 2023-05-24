package cn.tedu.codeadmin.service.impl;

import cn.tedu.codeadmin.mapper.RoleMapper;
import cn.tedu.codeadmin.pojo.vo.RoleListItemVO;
import cn.tedu.codeadmin.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    RoleMapper roleMapper;

    @Override
    public List<RoleListItemVO> list() {
        log.debug("開始處理【查詢角色列表】的業務");
        List<RoleListItemVO> list = roleMapper.list();
        Iterator<RoleListItemVO> iterator = list.iterator();
        while (iterator.hasNext()) {
            RoleListItemVO item = iterator.next();
            if (item.getId() == 1) {
                iterator.remove();
                break;
            }
        }
        return list;
    }

}
