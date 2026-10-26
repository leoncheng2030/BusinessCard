<template>
	<xn-form-container
		:title="formData.id ? '编辑用户' : '增加用户'"
		:width="800"
		:visible="visible"
		:destroy-on-close="true"
		:body-style="{ 'padding-top': '0px' }"
		@close="onClose"
	>
		<a-form ref="formRef" :model="formData" :rules="formRules" layout="vertical">
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="账号：" name="account">
						<a-input v-model:value="formData.account" placeholder="请输入账号" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="姓名：" name="name">
						<a-input v-model:value="formData.name" placeholder="请输入姓名" allow-clear />
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="性别：" name="gender">
						<a-radio-group v-model:value="formData.gender" :options="genderOptions"> </a-radio-group>
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="昵称：" name="nickname">
						<a-input v-model:value="formData.nickname" placeholder="请输入昵称" allow-clear />
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="手机号：" name="phone">
						<a-input v-model:value="formData.phone" placeholder="请输入手机" allow-clear />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="邮箱：" name="email">
						<a-input v-model:value="formData.email" placeholder="请输入邮箱" allow-clear />
					</a-form-item>
				</a-col>
			</a-row>
			<a-row :gutter="16">
				<a-col :span="12">
					<a-form-item label="出生日期：" name="birthday">
						<a-date-picker v-model:value="formData.birthday" value-format="YYYY-MM-DD" style="width: 100%" />
					</a-form-item>
				</a-col>
				<a-col :span="12">
					<a-form-item label="年龄：" name="age">
						<a-input-number v-model:value="formData.age" placeholder="请输入年龄" allow-clear style="width: 100%" />
					</a-form-item>
				</a-col>
			</a-row>
		</a-form>
		<template #footer>
			<a-button style="margin-right: 8px" @click="onClose">关闭</a-button>
			<a-button type="primary" :loading="formLoading" @click="onSubmit">保存</a-button>
		</template>
	</xn-form-container>
</template>

<script setup>
	import { cloneDeep } from 'lodash-es'
	import clientUserApi from '@/api/client/clientUserApi'
	import { required } from '@/utils/formRules'
	import tool from '@/utils/tool'
	// 默认是关闭状态
	const visible = ref(false)
	const formRef = ref()
	const emit = defineEmits({ successful: null })
	const formLoading = ref(false)
	// 表单数据
	const formData = ref({})
	// 打开抽屉
	const onOpen = (record) => {
		visible.value = true
		if (record) {
			let recordData = cloneDeep(record)
			formData.value = Object.assign({}, recordData)
		} else {
			formData.value = {
				gender: '1'
			}
		}
	}
	// 关闭抽屉
	const onClose = () => {
		formRef.value.resetFields()
		formData.value = {}
		visible.value = false
	}
	// 默认要校验的
	const formRules = {
		account: [required('请输入账号')],
		name: [required('请输入姓名')],
		gender: [required('请选择性别')]
	}
	// 验证并提交数据
	const onSubmit = () => {
		formRef.value
			.validate()
			.then(() => {
				clientUserApi
					.submitForm(formData.value, formData.value.id)
					.then(() => {
						onClose()
						emit('successful')
					})
					.finally(() => {
						formLoading.value = false
					})
			})
			.catch(() => {})
	}
	// 性别（字典 GENDER：0-未知 1-男 2-女）
	const genderOptions = tool.dictList('GENDER')

	// 调用这个函数将子组件的一些数据和方法暴露出去
	defineExpose({
		onOpen
	})
</script>

<style scoped lang="less">
	.form-row {
		background-color: var(--item-hover-bg);
		margin-left: 0 !important;
		margin-bottom: 10px;
	}
	.form-row-con {
		padding-bottom: 5px;
		padding-top: 5px;
		padding-left: 15px;
	}
</style>
