package com.atide.treeview.bean;


import com.atide.treeview.tree.bean.TreeNodeId;
import com.atide.treeview.tree.bean.TreeNodeLabel;
import com.atide.treeview.tree.bean.TreeNodePid;
import com.atide.treeview.tree.bean.TreeNodeTag;

public class Bean
{
	@TreeNodeId
	private String id;
	@TreeNodePid
	private String pId;
	@TreeNodeLabel
	private String label;
	@TreeNodeTag
	private Object tag;

	public Bean()
	{
	}

	public Bean(String id, String pId, String label)
	{
		this.id = id;
		this.pId = pId;
		this.label = label;
	}

	public Bean(String id, String pId, String label,Object tag)
	{
		this.id = id;
		this.pId = pId;
		this.label = label.replaceAll("\n","");
		this.tag = tag;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getpId()
	{
		return pId;
	}

	public void setpId(String pId)
	{
		this.pId = pId;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public Object getTag() {
		return tag;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}
}
