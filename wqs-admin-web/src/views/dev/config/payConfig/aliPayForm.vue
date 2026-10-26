<template>
	<a-spin :spinning="loadSpinning">
		<a-form
			ref="formRef"
			:model="formData"
			:rules="formRules"
			layout="vertical"
			:label-col="{ ...layout.labelCol, offset: 0 }"
			:wrapper-col="{ ...layout.wrapperCol, offset: 0 }"
		>
			<a-row :gutter="8">
				<a-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
					<a-form-item label="应用编号：" name="SNOWY_PAY_ALI_APP_ID">
						<a-input v-model:value="formData.SNOWY_PAY_ALI_APP_ID" placeholder="请输入支付宝应用编号" />
					</a-form-item>
				</a-col>
				<a-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
					<a-form-item label="应用私钥：" name="SNOWY_PAY_ALI_PRIVATE_KEY">
						<a-textarea
							v-model:value="formData.SNOWY_PAY_ALI_PRIVATE_KEY"
							placeholder="请输入应用私钥（PEM格式）"
							:auto-size="{ minRows: 4, maxRows: 12 }"
						/>
					</a-form-item>
				</a-col>
				<a-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
					<a-form-item label="商家用户ID：" name="SNOWY_PAY_ALI_MCH_USER_ID">
						<a-input v-model:value="formData.SNOWY_PAY_ALI_MCH_USER_ID" placeholder="请输入商家用户ID" />
					</a-form-item>
				</a-col>
				<a-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
					<a-form-item label="支付宝公钥证书路径：" name="SNOWY_PAY_ALI_CERT_PATH">
						<a-input v-model:value="formData.SNOWY_PAY_ALI_CERT_PATH" placeholder="请输入支付宝公钥证书路径" />
					</a-form-item>
				</a-col>
				<a-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
					<a-form-item label="支付宝根证书路径：" name="SNOWY_PAY_ALI_ROOT_CERT_PATH">
						<a-input v-model:value="formData.SNOWY_PAY_ALI_ROOT_CERT_PATH" placeholder="请输入支付宝根证书路径" />
					</a-form-item>
				</a-col>
				<a-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
					<a-form-item label="应用公钥证书路径：" name="SNOWY_PAY_ALI_APP_CERT_PATH">
						<a-input v-model:value="formData.SNOWY_PAY_ALI_APP_CERT_PATH" placeholder="请输入应用公钥证书路径" />
					</a-form-item>
				</a-col>
				<a-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
					<a-form-item label="支付网关：" name="SNOWY_PAY_ALI_SERVER_URL">
						<a-input v-model:value="formData.SNOWY_PAY_ALI_SERVER_URL" placeholder="留空使用默认生产网关" />
					</a-form-item>
				</a-col>
				<a-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
					<a-form-item label="支付回调地址：" name="SNOWY_PAY_ALI_NOTIFY_URL">
						<a-input v-model:value="formData.SNOWY_PAY_ALI_NOTIFY_URL" placeholder="请输入完整对外可访问URL，如 https://your.domain.com/pay/ali/notifyUrl" />
					</a-form-item>
				</a-col>
				<a-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
					<a-form-item>
						<a-space>
							<a-button type="primary" :loading="submitLoading" @click="onSubmit()">保存</a-button>
							<a-button @click="() => formRef.resetFields()">重置</a-button>
						</a-space>
					</a-form-item>
				</a-col>
			</a-row>
		</a-form>
	</a-spin>
</template>

<script setup name="aliPayForm">
	import { cloneDeep } from 'lodash-es'
	import { required } from '@/utils/formRules'
	import { message } from 'ant-design-vue'
	import configApi from '@/api/dev/configApi'

	const formRef = ref()
	const formData = ref({})
	const submitLoading = ref(false)
	const loadSpinning = ref(true)

	// 查询此界面的配置项,并转为表单
	const param = {
		category: 'PAY_ALI'
	}
	configApi.configList(param).then((data) => {
		loadSpinning.value = false
		if (data) {
			data.forEach((item) => {
				formData.value[item.configKey] = item.configValue
			})
		} else {
			message.warning('表单项不存在，请初始化数据库')
		}
	})

	// 默认要校验的（支付网关可留空使用默认生产网关）
	const formRules = {
		SNOWY_PAY_ALI_APP_ID: [required('请输入应用编号')],
		SNOWY_PAY_ALI_PRIVATE_KEY: [required('请输入应用私钥')],
		SNOWY_PAY_ALI_MCH_USER_ID: [required('请输入商家用户ID')],
		SNOWY_PAY_ALI_CERT_PATH: [required('请输入支付宝公钥证书路径')],
		SNOWY_PAY_ALI_ROOT_CERT_PATH: [required('请输入支付宝根证书路径')],
		SNOWY_PAY_ALI_APP_CERT_PATH: [required('请输入应用公钥证书路径')],
		SNOWY_PAY_ALI_NOTIFY_URL: [required('请输入支付回调地址')]
	}
	// 验证并提交数据
	const onSubmit = () => {
		formRef.value
			.validate()
			.then(() => {
				submitLoading.value = true
				let submitParam = cloneDeep(formData.value)
				const param = Object.entries(submitParam)
					// 过滤空值项，避免后端必填校验拒绝
					.filter((item) => item[1] !== undefined && item[1] !== null && item[1] !== '')
					.map((item) => {
						return {
							configKey: item[0],
							configValue: item[1]
						}
					})
				configApi
					.configEditForm(param)
					.then(() => {
						message.success('保存成功')
					})
					.finally(() => {
						submitLoading.value = false
					})
			})
			.catch(() => {})
	}
	const layout = {
		labelCol: {
			span: 24
		},
		wrapperCol: {
			span: 12
		}
	}
</script>
