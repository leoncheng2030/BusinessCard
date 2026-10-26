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
					<a-form-item label="应用编号：" name="SNOWY_PAY_WX_APP_ID">
						<a-input v-model:value="formData.SNOWY_PAY_WX_APP_ID" placeholder="请输入微信应用编号" />
					</a-form-item>
				</a-col>
				<a-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
					<a-form-item label="AppSecret：" name="SNOWY_PAY_WX_APP_SECRET">
						<a-input v-model:value="formData.SNOWY_PAY_WX_APP_SECRET" placeholder="请输入AppSecret" />
					</a-form-item>
				</a-col>
				<a-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
					<a-form-item label="商户号：" name="SNOWY_PAY_WX_MCH_ID">
						<a-input v-model:value="formData.SNOWY_PAY_WX_MCH_ID" placeholder="请输入商户号" />
					</a-form-item>
				</a-col>
				<a-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
					<a-form-item label="商户V2密钥：" name="SNOWY_PAY_WX_MCH_KEY">
						<a-input v-model:value="formData.SNOWY_PAY_WX_MCH_KEY" placeholder="请输入商户V2密钥" />
					</a-form-item>
				</a-col>
				<a-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
					<a-form-item label="p12证书路径：" name="SNOWY_PAY_WX_KEY_PATH">
						<a-input v-model:value="formData.SNOWY_PAY_WX_KEY_PATH" placeholder="请输入p12证书路径" />
					</a-form-item>
				</a-col>
				<a-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
					<a-form-item label="pub_key.pem证书路径：" name="SNOWY_PAY_WX_PUB_KEY_PATH">
						<a-input v-model:value="formData.SNOWY_PAY_WX_PUB_KEY_PATH" placeholder="请输入pub_key.pem证书路径" />
					</a-form-item>
				</a-col>
				<a-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
					<a-form-item label="公钥ID：" name="SNOWY_PAY_WX_PUB_KEY_ID">
						<a-input v-model:value="formData.SNOWY_PAY_WX_PUB_KEY_ID" placeholder="请输入公钥ID" />
					</a-form-item>
				</a-col>
				<a-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
					<a-form-item label="apiclient_key.pem路径：" name="SNOWY_PAY_WX_PRIVATE_KEY_PATH">
						<a-input v-model:value="formData.SNOWY_PAY_WX_PRIVATE_KEY_PATH" placeholder="请输入apiclient_key.pem路径" />
					</a-form-item>
				</a-col>
				<a-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
					<a-form-item label="apiclient_cert.pem路径：" name="SNOWY_PAY_WX_PRIVATE_CERT_PATH">
						<a-input v-model:value="formData.SNOWY_PAY_WX_PRIVATE_CERT_PATH" placeholder="请输入apiclient_cert.pem路径" />
					</a-form-item>
				</a-col>
				<a-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
					<a-form-item label="API V3证书序列号：" name="SNOWY_PAY_WX_CERT_SERIAL_NO">
						<a-input v-model:value="formData.SNOWY_PAY_WX_CERT_SERIAL_NO" placeholder="请输入API V3证书序列号" />
					</a-form-item>
				</a-col>
				<a-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
					<a-form-item label="API V3秘钥值：" name="SNOWY_PAY_WX_API_V3_KEY">
						<a-input v-model:value="formData.SNOWY_PAY_WX_API_V3_KEY" placeholder="请输入API V3秘钥值" />
					</a-form-item>
				</a-col>
				<a-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
					<a-form-item label="支付回调地址：" name="SNOWY_PAY_WX_NOTIFY_URL">
						<a-input v-model:value="formData.SNOWY_PAY_WX_NOTIFY_URL" placeholder="请输入完整对外可访问URL，如 https://your.domain.com/pay/wx/notifyUrl" />
					</a-form-item>
				</a-col>
				<a-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
					<a-form-item label="退款回调地址：" name="SNOWY_PAY_WX_REFUND_NOTIFY_URL">
						<a-input v-model:value="formData.SNOWY_PAY_WX_REFUND_NOTIFY_URL" placeholder="请输入完整对外可访问URL，如 https://your.domain.com/pay/wx/refundNotifyUrl" />
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

<script setup name="wxPayForm">
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
		category: 'PAY_WX'
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

	// 默认要校验的
	const formRules = {
		SNOWY_PAY_WX_APP_ID: [required('请输入应用编号')],
		SNOWY_PAY_WX_APP_SECRET: [required('请输入AppSecret')],
		SNOWY_PAY_WX_MCH_ID: [required('请输入商户号')],
		SNOWY_PAY_WX_MCH_KEY: [required('请输入商户V2密钥')],
		SNOWY_PAY_WX_KEY_PATH: [required('请输入p12证书路径')],
		SNOWY_PAY_WX_PUB_KEY_PATH: [required('请输入pub_key.pem证书路径')],
		SNOWY_PAY_WX_PUB_KEY_ID: [required('请输入公钥ID')],
		SNOWY_PAY_WX_PRIVATE_KEY_PATH: [required('请输入apiclient_key.pem路径')],
		SNOWY_PAY_WX_PRIVATE_CERT_PATH: [required('请输入apiclient_cert.pem路径')],
		SNOWY_PAY_WX_CERT_SERIAL_NO: [required('请输入API V3证书序列号')],
		SNOWY_PAY_WX_API_V3_KEY: [required('请输入API V3秘钥值')],
		SNOWY_PAY_WX_NOTIFY_URL: [required('请输入支付回调地址')],
		SNOWY_PAY_WX_REFUND_NOTIFY_URL: [required('请输入退款回调地址')]
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
