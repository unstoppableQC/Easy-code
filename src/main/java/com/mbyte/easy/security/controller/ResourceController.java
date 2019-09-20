package com.mbyte.easy.security.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mbyte.easy.entity.SysResource;
import com.mbyte.easy.entity.SysRole;
import com.mbyte.easy.mapper.SysResourceMapper;
import com.mbyte.easy.util.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目权限资源控制类
 * 
 * @author 秦策
 *
 */
@Controller
@Transactional
@RequestMapping("/resource")
public class ResourceController {
	private static final Logger logger = LoggerFactory.getLogger(ResourceController.class);

	@Autowired
	private SysResourceMapper resourceMapper;

	@ModelAttribute("types")
	public SysResource.ResourceType[] resourceTypes() {
		return SysResource.ResourceType.values();
	}

	@ModelAttribute("resourceList")
	public List<SysResource> resourceList() {
		SysResource resource = new SysResource();
		resource.setType(0);
		List<SysResource> resourceList = resourceMapper.selectByResource(resource);
		return resourceList;
	}

	/**
	 * 进入资源管理界面
	 * 
	 * @return
	 */
	@PreAuthorize("hasAuthority('/resource')")
	@RequestMapping(value = { "", "/" })
	public String index(Model model,
			@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
			@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
			@RequestParam(value = "name", required = false, defaultValue = "") String name) {
		SysResource resource = new SysResource();
		if (name != null && !"".equals(name.trim())) {
			resource.setName(name.trim());
			model.addAttribute("name", name.trim());
		}
		Page<SysResource> page = new Page<SysResource>(pageNo, pageSize);
		IPage<SysResource> pageInfo = resourceMapper.selectBySysResourceForPage(page, resource);
		model.addAttribute("pageInfo", new PageInfo<SysResource>(pageInfo));

		return "admin-permission";
	}

	/**
	 * 添加资源权限
	 * 
	 * @param model
	 * @param resource
	 * @return
	 */
	@PreAuthorize("hasAuthority('/resource/add-permission')")
	@RequestMapping(value = "/add-permission")
	public String addPermissionBefore(Model model, @ModelAttribute(value = "resource") SysResource resource) {

		return "admin-add-permission";
	}

	@ResponseBody
	@RequestMapping(value = "/add-permission", params = "save=true")
	public String addPermission(Model model, @ModelAttribute(value = "resource") SysResource resource) {
		if (resource != null && resource.getName() != null && !"".equals(resource.getName())) {
			resourceMapper.insert(resource);
			return "1";
		}
		return "0";
	}

	/**
	 * 编辑资源权限
	 * 
	 * @param model
	 * @param resource
	 * @return
	 */
	@PreAuthorize("hasAuthority('/resource/editor-permission')")
	@RequestMapping(value = "/editor-permission/{id}")
	public String editorPermissionBefore(Model model, @ModelAttribute(value = "resource") SysResource resource,
			@PathVariable("id") Long id) {
		resource = resourceMapper.selectByPrimaryKey(id);
		model.addAttribute("resource", resource);
		return "admin-editor-permission";
	}

	@ResponseBody
	@RequestMapping(value = "/editor-permission", params = "update=true")
	public String editorPermission(Model model, @ModelAttribute(value = "resource") SysResource resource) {
		if (resource != null && resource.getName() != null && !"".equals(resource.getName())) {
			resourceMapper.updateByPrimaryKeySelective(resource);
			return "1";
		}
		return "0";
	}

	/**
	 * 删除资源
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@PreAuthorize("hasAuthority('/resource/delete')")
	@Transactional
	@ResponseBody
	@RequestMapping(value = "/delete", produces = "application/json", consumes = "application/json")
	public Integer delete(@RequestBody Long[] ids) {
		try {
			for (long id : ids) {
				resourceMapper.deleteByPrimaryKey(id);
			}
			return 1;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return 0;
	}
}
