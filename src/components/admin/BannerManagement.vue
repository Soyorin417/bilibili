<template>
  <div class="banner-management">
    <div class="banner-header">
      <h4>轮播图管理</h4>
      <div class="header-actions">
        <select
          v-model="currentCategory"
          class="form-select me-3"
          style="width: 150px"
          @change="$emit('type-change', currentCategory)"
        >
          <option value="home">首页轮播</option>
          <option value="anime">动漫轮播</option>
        </select>
        <button class="btn btn-primary" @click="showAddModal = true">添加轮播图</button>
      </div>
    </div>

    <!-- 轮播图列表 -->
    <div class="banner-list">
      <table class="table">
        <thead>
          <tr>
            <th>预览</th>
            <th>分类</th>
            <th>ID</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="banner in banners" :key="banner.id">
            <td>
              <img :src="banner.url" class="banner-preview" alt="轮播图" />
            </td>
            <td>{{ banner.category === "home" ? "首页" : "动漫" }}</td>
            <td>{{ banner.id }}</td>
            <td>
              <button class="btn btn-info btn-sm me-2" @click="handleEdit(banner)">
                编辑
              </button>
              <button class="btn btn-danger btn-sm" @click="handleDelete(banner.id)">
                删除
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 添加/编辑轮播图弹窗 -->
    <div v-if="showAddModal || showEditModal" class="modal-mask">
      <div class="modal-wrapper">
        <div class="modal-container">
          <h5>{{ showEditModal ? "编辑轮播图" : "添加轮播图" }}</h5>
          <form @submit.prevent="handleSubmit">
            <div class="form-group mb-3">
              <label>分类</label>
              <select v-model="formData.category" class="form-control" required>
                <option value="home">首页轮播</option>
                <option value="anime">动漫轮播</option>
              </select>
            </div>

            <div class="form-group mb-3">
              <label>图片</label>
              <input
                type="file"
                @change="handleImageChange"
                class="form-control"
                accept="image/*"
                :required="!showEditModal"
              />
            </div>

            <div class="form-actions">
              <button type="submit" class="btn btn-primary">
                {{ showEditModal ? "保存" : "添加" }}
              </button>
              <button type="button" class="btn btn-secondary ms-2" @click="closeModal">
                取消
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "BannerManagement",
  props: {
    banners: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      showAddModal: false,
      showEditModal: false,
      currentCategory: "home",
      currentId: null,
      uploadProgress: 0,
      formData: {
        image: null,
      },
    };
  },
  methods: {
    handleImageChange(event) {
      const file = event.target.files[0];
      if (file) {
        this.formData.image = file;
      }
    },
    handleEdit(banner) {
      this.currentId = banner.id;
      this.formData = {
        image: null,
      };
      this.showEditModal = true;
    },
    handleDelete(id) {
      this.$emit("delete", id);
    },
    async handleSubmit() {
      if (!this.formData.image) {
        this.$message.error("请选择图片");
        return;
      }

      const formData = new FormData();
      formData.append("image", this.formData.image);

      if (this.currentId) {
        // 更新轮播图
        this.$emit("update", this.currentId, formData);
      } else {
        // 添加轮播图
        this.$emit("add", formData);
      }

      this.closeModal();
    },
    closeModal() {
      this.showAddModal = false;
      this.showEditModal = false;
      this.currentId = null;
      this.formData = {
        image: null,
      };
      this.uploadProgress = 0;
    },
    resetForm() {
      this.formData = {
        image: null,
      };
      this.currentId = null;
    },
  },
};
</script>

<style scoped>
.banner-management {
  padding: 20px;
}

.banner-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-actions {
  display: flex;
  align-items: center;
}

.banner-list {
  background: #fff;
}

.banner-preview {
  width: 100px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
}

.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-wrapper {
  width: 100%;
  max-width: 500px;
}

.modal-container {
  background: #fff;
  padding: 24px;
  border-radius: 8px;
}

.image-preview {
  max-width: 200px;
  max-height: 120px;
  object-fit: contain;
}

.form-actions {
  margin-top: 24px;
  text-align: right;
}
</style>
