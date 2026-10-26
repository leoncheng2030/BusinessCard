<template>
	<a-spin :spinning="loadSpinning">
		<a-form :model="formData" layout="vertical" style="width: 92%; margin-top: 8px">
			<a-row :gutter="16">
				<a-col v-for="field in fieldList" :key="field.key" :xs="24" :sm="24" :md="24" :lg="24">
					<a-form-item :label="field.label" :name="field.key">
						<a-input
							v-if="field.type === 'input' || field.type === 'url'"
							v-model:value="formData[field.key]"
							:placeholder="'请输入' + field.label"
							allow-clear
						/>
						<a-input-password
							v-else-if="field.type === 'password'"
							v-model:value="formData[field.key]"
							:placeholder="'请输入' + field.label"
							allow-clear
						/>
						<a-textarea
							v-else-if="field.type === 'textarea'"
							v-model:value="formData[field.key]"
							:placeholder="'请输入' + field.label"
							:auto-size="{ minRows: 2, maxRows: 6 }"
						/>
						<a-upload
							v-else-if="field.type === 'image'"
							class="avatar-uploader"
							list-type="picture-card"
							:show-upload-list="false"
							:custom-request="(e) => customRequest(e, field)"
							accept="image/png, image/jpeg, image/jpg"
						>
							<img
								v-if="formData[field.key]"
								:src="formData[field.key]"
								alt="avatar"
								style="max-height: 100px; max-width: 100px"
							/>
							<div v-else>
								<plus-outlined />
								<div class="ant-upload-text">上传</div>
							</div>
						</a-upload>
					</a-form-item>
				</a-col>
			</a-row>
			<a-space>
				<a-button type="primary" :loading="submitLoading" @click="onSubmit">保存</a-button>
				<a-button @click="loadData">重置</a-button>
			</a-space>
		</a-form>
	</a-spin>
</template>

<script setup name="clientShowForm">
	import { cloneDeep } from 'lodash-es'
	import { message } from 'ant-design-vue'
	import configApi from '@/api/dev/configApi'

	const props = defineProps({
		category: {
			type: String,
			required: true
		}
	})

	// 各端字段定义（与 DEV_CONFIG 中 CLIENT_SHOW_* 分类的 key 一致）
	const FIELD_SCHEMA = {
		CLIENT_SHOW_MINI: [
			{ key: 'CLIENT_SHOW_MINI_NAME', label: '小程序名称', type: 'input', required: true },
			{ key: 'CLIENT_SHOW_MINI_LOGO', label: '小程序LOGO', type: 'image' },
			{ key: 'CLIENT_SHOW_MINI_DESCRIPTION', label: '小程序简介描述', type: 'textarea' },
			{ key: 'CLIENT_SHOW_MINI_APP_ID', label: '小程序AppID', type: 'input' },
			{ key: 'CLIENT_SHOW_MINI_APP_SECRET', label: '小程序AppSecret', type: 'password' },
			{ key: 'CLIENT_SHOW_MINI_QR_CODE', label: '小程序码', type: 'image' },
			{ key: 'CLIENT_SHOW_MINI_SERVICE_TEL', label: '小程序客服电话', type: 'input' },
			{ key: 'CLIENT_SHOW_MINI_OFFICIAL_URL', label: '小程序官网地址', type: 'input' },
			{ key: 'CLIENT_SHOW_MINI_COPYRIGHT', label: '小程序版权信息', type: 'input' }
		],
		CLIENT_SHOW_MP: [
			{ key: 'CLIENT_SHOW_MP_NAME', label: '公众号名称', type: 'input', required: true },
			{ key: 'CLIENT_SHOW_MP_LOGO', label: '公众号LOGO', type: 'image' },
			{ key: 'CLIENT_SHOW_MP_DESCRIPTION', label: '公众号简介描述', type: 'textarea' },
			{ key: 'CLIENT_SHOW_MP_APP_ID', label: '公众号AppID', type: 'input' },
			{ key: 'CLIENT_SHOW_MP_APP_SECRET', label: '公众号AppSecret', type: 'password' },
			{ key: 'CLIENT_SHOW_MP_QR_CODE', label: '公众号二维码', type: 'image' },
			{ key: 'CLIENT_SHOW_MP_SERVICE_TEL', label: '公众号客服电话', type: 'input' },
			{ key: 'CLIENT_SHOW_MP_OFFICIAL_URL', label: '公众号官网地址', type: 'input' },
			{ key: 'CLIENT_SHOW_MP_COPYRIGHT', label: '公众号版权信息', type: 'input' }
		],
		CLIENT_SHOW_APP: [
			{ key: 'CLIENT_SHOW_APP_NAME', label: 'APP名称', type: 'input', required: true },
			{ key: 'CLIENT_SHOW_APP_LOGO', label: 'APP图标', type: 'image' },
			{ key: 'CLIENT_SHOW_APP_DESCRIPTION', label: 'APP简介描述', type: 'textarea' },
			{ key: 'CLIENT_SHOW_APP_APP_ID', label: '应用标识（包名）', type: 'input' },
			{ key: 'CLIENT_SHOW_APP_APP_SECRET', label: '应用密钥', type: 'password' },
			{ key: 'CLIENT_SHOW_APP_QR_CODE', label: 'APP下载二维码', type: 'image' },
			{ key: 'CLIENT_SHOW_APP_SERVICE_TEL', label: 'APP客服电话', type: 'input' },
			{ key: 'CLIENT_SHOW_APP_OFFICIAL_URL', label: 'APP官网地址', type: 'input' },
			{ key: 'CLIENT_SHOW_APP_DOWNLOAD_URL', label: 'APP下载地址', type: 'input' },
			{ key: 'CLIENT_SHOW_APP_VERSION', label: '当前版本号', type: 'input' },
			{ key: 'CLIENT_SHOW_APP_COPYRIGHT', label: 'APP版权信息', type: 'input' }
		]
	}

	const fieldList = computed(() => FIELD_SCHEMA[props.category])
	// 必填字段（名称）
	const nameField = computed(() => fieldList.value.find((item) => item.required))

	const formData = ref({})
	const loadSpinning = ref(true)
	const submitLoading = ref(false)

	// 加载该分类配置并转为表单
	const loadData = () => {
		formData.value = {}
		loadSpinning.value = true
		configApi
			.configList({ category: props.category })
			.then((data) => {
				const valueMap = {}
				;(data || []).forEach((item) => {
					valueMap[item.configKey] = item.configValue || ''
				})
				formData.value = valueMap
			})
			.finally(() => {
				loadSpinning.value = false
			})
	}
	loadData()

	// 图片文件转base64后直接存库展示
	const customRequest = (data, field) => {
		getBase64(data.file)
			.then((res) => {
				formData.value[field.key] = res
			})
			.catch(() => {})
	}

	const getBase64 = (file) => {
		return new Promise((resolve, reject) => {
			const reader = new FileReader()
			reader.readAsDataURL(file)
			reader.onload = () => resolve(reader.result)
			reader.onerror = (error) => reject(error)
		})
	}

	// 保存当前端配置（仅更新已存在的行，不做新增）
	const onSubmit = () => {
		if (!formData.value[nameField.value.key]) {
			message.warning('请输入' + nameField.value.label)
			return
		}
		submitLoading.value = true
		const param = fieldList.value.map((item) => {
			return {
				configKey: item.key,
				configValue: cloneDeep(formData.value[item.key] || '')
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
	}
</script>
